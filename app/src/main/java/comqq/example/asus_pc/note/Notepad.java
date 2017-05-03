package comqq.example.asus_pc.note;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by asus-pc on 2017/5/3.
 */

public class Notepad extends DataSupport{
    private String title;
    private Date time;
    private String centent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCentent() {
        return centent;
    }

    public void setCentent(String centent) {
        this.centent = centent;
    }
}
