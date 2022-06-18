# imgcaptureupload

#### This project can be used to capture image from the android camera or you can also pick a photo from the galery and upload it to the Digital Ocean Spaces.
#### The digital ocean spaces can be configured in SpaceRegion.java class accordingly. 
#### The camera capture library used here is Dhaval's ImagePicker. 🔗https://github.com/Dhaval2404/ImagePicker

#### This repo uses the AWS SDK to connect with digital ocean spaces and uploads the image file selected using an AsyncTask in SpaceRegion.java. 🔗https://github.com/aws-amplify/aws-sdk-android

#### Compatibility options
##### compileOptions {
#####   sourceCompatibility JavaVersion.VERSION_1_8
#####   targetCompatibility JavaVersion.VERSION_1_8
##### }
