const functions = require("firebase-functions");
const { Storage } = require("@google-cloud/storage");
const admin = require("firebase-admin");
const gcs = new Storage();
const sharp = require("sharp");
const fs = require("fs-extra");
const os = require("os");
const path = require("path");
const uuid = require('uuid');
admin.initializeApp(functions.config().firebase);

/*exports.sendNotification = functions.firestore.document("Activities/{activity_id}").onWrite((change, context) =>{

    console.log("params: "+context.params.OwnerID)
});

exports.createUser = functions.firestore
    .document("Activities/{activity_id}")
    .onCreate((snap, context) => {
        const newValue = snap.data();
        const name = newValue.OwnerID;
        console.log("Name: "+name)

    });*/

exports.updateUser = functions.firestore
    .document('Activities/{activity_id}')
    .onUpdate((change, context) => {
        const newValue = change.after.data();
        const previousValue = change.before.data();
        const payload = {
            notification:{
                title:"",
                body: ""
            }
        }

       if(previousValue.State == "asked" && newValue.State == "accepted"){
           console.log("Hey your Offer is accepted!")
           payload.notification.title = "Hey your Offer is accepted!";
           payload.notification.body = "Your offer for "+newValue.Title + " is accepted by the Vendor!"
       }
        if(previousValue.State == "asked" && newValue.State == "rejected"){
            console.log("Hey your Offer is rejected!")
            payload.notification.title = "Hey your Offer is rejected!";
            payload.notification.body = "Your offer for "+newValue.Title + " is sadly rejected by the Vendor!"

        }
        console.log("OwnerID: "+newValue.OwnerID);
        console.log("UserID: "+newValue.UserID);
        admin.messaging().sendToDevice(newValue.tokenID,payload).then(res=>{
            console.log("Notification send!")
        })
    });


exports.resizeImages = functions.storage.object().onFinalize(async object => {
    try {
        const uniqueName = uuid.v1();
        const bucket = gcs.bucket(object.bucket);

        const filePath = object.name;
        const fileName = filePath.split("/").pop();
        const bucketDir = path.dirname(filePath);

        const workingDir = path.join(os.tmpdir(), `images_${uniqueName}`);
        const tmpFilePath = path.join(workingDir, `source_${uniqueName}.png`);

        if (fileName.includes("image@") || !object.contentType.includes("image")) {
            console.log("Exiting image resizer!");
            return false;
        }

        await fs.ensureDir(workingDir);
        await bucket.file(filePath).download({
            destination: tmpFilePath
        });

        const sizes = [128, 256, 300, 600];
        const uploadPromises = sizes.map(async size => {
            const thumbName = `image@${size}_${fileName}`;
            const thumbPath = path.join(workingDir, thumbName);


            if (size < 300) {
                await sharp(tmpFilePath)
                    .resize(size, size)
                    .toFile(thumbPath);
            } else {
                let height = Math.floor(size * 0.5625);

                await sharp(tmpFilePath)
                    .resize(size, height)
                    .toFile(thumbPath);
            }
            return bucket.upload(thumbPath, {
                destination: path.join(bucketDir, thumbName)
            });
        });

        await Promise.all(uploadPromises);
        await fs.remove(workingDir);
        await fs.remove(bucketDir);

        return Promise.resolve();
    } catch (error) {
        return Promise.reject(error);
    }
});