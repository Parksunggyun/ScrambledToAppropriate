package al.tong.mon.scrambledtoappropriate.one;

public class One {

    private int image;
    private int tag;
    private boolean empty;

    One(int image, int tag, boolean empty) {
        this.image = image;
        this.tag = tag;
        this.empty = empty;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public int getImage() {
        return image;
    }

    public int getTag() {
        return tag;
    }

    public boolean isEmpty() {
        return empty;
    }
}
