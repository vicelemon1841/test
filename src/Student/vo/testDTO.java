package Student.vo;

public class testDTO {
    //시험 탭의 table 정보들을 불러와야됨.
    private String lec_name, ad_name, lec_no, test_name;
    // 강의명, 강사명, 강의코드, 시험명

    public String getLec_name() {
        return lec_name;
    }

    public void setLec_name(String lec_name) {
        this.lec_name = lec_name;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getLec_no() {
        return lec_no;
    }

    public void setLec_no(String lec_no) {
        this.lec_no = lec_no;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }
}
