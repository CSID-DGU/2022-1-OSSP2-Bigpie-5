package kr.co.bigpie.flying.Letter;

public class MemoItem {
    String date;
    String title;
    String content;
    String reserve;

    public MemoItem(String date, String title, String content, String reserve) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.reserve = reserve;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {this.content = content;}

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {this.reserve = reserve;}
}