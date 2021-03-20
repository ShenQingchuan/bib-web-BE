package pro.techdict.bib.bibserver.models;

public enum DUPLICATE_TYPES {
  PASS,
  WITH_USERNAME {
    @Override
    public java.lang.String toString() {
      return "该用户名已经被注册！";
    }
  },
  WITH_EMAIL {
    @Override
    public java.lang.String toString() {
      return "该邮箱地址已经被注册！";
    }
  },
  WITH_PHONE {
    @Override
    public java.lang.String toString() {
      return "该手机号码已经被注册！";
    }
  },
}
