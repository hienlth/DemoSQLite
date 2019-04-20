package info.hienlth.qlsv;

public class Lop {
    private String MaLop;
    private String TenLop;
    private int SiSo;

    public Lop(String maLop, String tenLop, int siSo) {
        MaLop = maLop;
        TenLop = tenLop;
        SiSo = siSo;
    }

    public String getMaLop() {
        return MaLop;
    }

    public void setMaLop(String maLop) {
        MaLop = maLop;
    }

    public String getTenLop() {
        return TenLop;
    }

    public void setTenLop(String tenLop) {
        TenLop = tenLop;
    }

    public int getSiSo() {
        return SiSo;
    }

    public void setSiSo(int siSo) {
        SiSo = siSo;
    }

    @Override
    public String toString() {
        return MaLop + "  - " + TenLop + " : " + SiSo;
    }
}
