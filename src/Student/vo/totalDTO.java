package Student.vo;

public class totalDTO {
    //이거는 이제 음... table1의 정보들 (강의실 탭의 table)
    // stdno를 통해서 강의명(lec_std_t) / 강사명(lec_adm_t), 강의 시작일(lec_adm_t), 강의 종료일(lec_t), 강의번호(임시)(lec_adm_t) 를 받아올거임
    
    private String lec_name,ad_name, ad_sdate, lec_no;

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

    public String getAd_sdate() {
        return ad_sdate;
    }

    public void setAd_sdate(String ad_sdate) {
        this.ad_sdate = ad_sdate;
    }

    public String getLec_no() {
        return lec_no;
    }

    public void setLec_no(String lec_no) {
        this.lec_no = lec_no;
    }
}
