//
var admin = require("firebase-admin");

//서비스 Account 경로수정
var serviceAccount = require("C:\Users\KohNahyung\Desktop\push notification\package-lock.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

//디바이스 토큰 받아오기
var registrationToken = "<registration token을 여기에 넣기>";

//알림 메시지 설정
var payload = {
    notification: {
        //푸시 알림 제목
        title: "나에게 보내는 편지 APP",
        //푸시 알림 내용
        body: "편지가 도착했습니다."
    }
}

//푸시 알림 보내기
admin.messaging().sendToDevice(registrationToken, payload, options)
    .then(function(response){
        console.log("Successfully sent message: ", response);
    })
    .catch(function(error){
        console.log("Error sending message: ", error);
    })