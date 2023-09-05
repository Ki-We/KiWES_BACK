# 진짜 서버 KiWES_BACKEND

java 19
spring boot 2.7.11
gradle-7.6.1

서버 : EC2
DB : Redis + (RDS)MySQL
storgy : S3

### 알림 TO-DO LIST

- [x] 알림 가져오기
- [x] 누군가가 모임에 참여 신청을 했을 때 ( ACCESS, AlarmContent.PARTICIPATE )
- [x] 누군가가 모임에 문의를 남겼을 때 ( CLUB, AlarmContent.QUESTION )
- [x] 누군가가 모임에 후기를 남겼을 때 ( CLUB, AlarmContent.REVIEW )
- [x] 누군가가 내 문의에 답변을 달았을 때 ( CLUB, AlarmContent.ANSWER )
- [x] 내 모임 신청이 승인되었을 때 ( CHATTING, AlarmContent.CHATTING )
- [x] 내가 모임에서 강퇴당했을 때 ( CLUB, AlarmContent.KICKOUT )

