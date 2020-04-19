namespace java com.mark.thrift.user

/**
  *  https://github.com/apache/thrift
  *
  *  bool        Boolean, one byte
  *  i8 (byte)   Signed 8-bit integer
  *  i16         Signed 16-bit integer
  *  i32         Signed 32-bit integer
  *  i64         Signed 64-bit integer
  *  double      64-bit floating point value
  *  string      String
  *  binary      Blob (byte array)
  *  map<t1,t2>  Map from one type to another
  *  list<t1>    Ordered list of one type
  *  set<t1>     Set of unique elements of one type
  **/
struct UserInfo {
    1:i32 id,
    2:string username,
    3:string password,
    4:string realName,
    5:string mobile,
    6:string email,
    7:string intro,
    8:i32 stars
}

service UserService {

    UserInfo getUserById(1:i32 id);

    UserInfo getTeacherById(1:i32 id);

    UserInfo getUserByName(1:string username);

    void registerUser(1:UserInfo userInfo);
}