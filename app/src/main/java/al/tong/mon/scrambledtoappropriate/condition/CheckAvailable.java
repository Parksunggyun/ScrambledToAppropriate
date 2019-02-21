package al.tong.mon.scrambledtoappropriate.condition;

import java.util.Vector;

import al.tong.mon.scrambledtoappropriate.one.One;

public class CheckAvailable {

    private int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4, NONE = 100;

    private Vector<One> mOne;

    private int lengthPos;
    private int spanPos;
    private int span;

    public CheckAvailable(Vector<One> ones, int span) {
        this.mOne = ones;
        this.lengthPos = ones.size() - 1;
        this.spanPos = span - 1;
        this.span = span;
    }

    public int[] check(int pos) {
        int[] result;
        if (pos == 0) { // left top
            result = leftTop(pos);
        } else if (pos == lengthPos - spanPos) { // left bottom
            result = leftBottom(pos);
        } else if (pos == spanPos) { //right top
            result = rightTop(pos);
        } else if (pos == lengthPos) { // right bottom
            result = rightBottom(pos);
        } else if (pos > 0 && pos < spanPos) { // top
            result = top(pos);
        } else if (pos > (lengthPos - spanPos) && pos < lengthPos) { // bottom
            result = bottom(pos);
        } else if (pos % span == 0 && pos != (lengthPos - spanPos)) { // left
            result = left(pos);
        } else if (pos % span == spanPos) { // right
            result = right(pos);
        } else {
            result = center(pos);
        }
        return result;
    }

    // up = 1, down = 2, left = 3, right = 4
    private int[] leftTop(int pos) {
        if (mOne.get(pos + 1).isEmpty()) {
            return new int[]{pos + 1, RIGHT};
        } else if (mOne.get(pos + span).isEmpty()) {
            return new int[]{pos + span, DOWN};
        } else {
            return new int[]{NONE, NONE};
        }
    }

    private int[] top(int pos) {
        if (mOne.get(pos + 1).isEmpty()) {
            return new int[]{pos + 1, RIGHT};
        } else if (mOne.get(pos - 1).isEmpty()) {
            return new int[]{pos - 1, LEFT};
        } else if (mOne.get(pos + span).isEmpty()) {
            return new int[]{pos + span, DOWN};
        } else {
            return new int[]{NONE, NONE};
        }
    }

    private int[] rightTop(int pos) {
        if (mOne.get(pos - 1).isEmpty()) {
            return new int[]{pos - 1, LEFT};
        } else if (mOne.get(pos + span).isEmpty()) {
            return new int[]{pos + span, DOWN};
        } else {
            return new int[]{NONE, NONE};
        }
    }

    private int[] left(int pos) {
        if (mOne.get(pos + 1).isEmpty()) {
            return new int[]{pos + 1, RIGHT};
        } else if (mOne.get(pos - span).isEmpty()) {
            return new int[]{pos - span, UP};
        } else if (mOne.get(pos + span).isEmpty()) {
            return new int[]{pos + span, DOWN};
        } else {
            return new int[]{NONE, NONE};
        }
    }

    private int[] center(int pos) {
        if (mOne.get(pos + 1).isEmpty()) {
            return new int[]{pos + 1, RIGHT};
        } else if (mOne.get(pos - 1).isEmpty()) {
            return new int[]{pos - 1, LEFT};
        } else if (mOne.get(pos + span).isEmpty()) {
            return new int[]{pos + span, DOWN};
        } else if (mOne.get(pos - span).isEmpty()) {
            return new int[]{pos - span, UP};
        } else {
            return new int[]{NONE, NONE};
        }
    }

    private int[] right(int pos) {
        if (mOne.get(pos - 1).isEmpty()) {
            return new int[]{pos - 1, LEFT};
        } else if (mOne.get(pos + span).isEmpty()) {
            return new int[]{pos + span, DOWN};
        } else if (mOne.get(pos - span).isEmpty()) {
            return new int[]{pos - span, UP};
        } else {
            return new int[]{NONE, NONE};
        }
    }

    private int[] leftBottom(int pos) {
        if (mOne.get(pos + 1).isEmpty()) {
            return new int[]{pos + 1, RIGHT};
        } else if (mOne.get(pos - span).isEmpty()) {
            return new int[]{pos - span, UP};
        } else {
            return new int[]{NONE, NONE};
        }
    }

    private int[] bottom(int pos) {
        if (mOne.get(pos + 1).isEmpty()) {
            return new int[]{pos + 1, RIGHT};
        } else if (mOne.get(pos - 1).isEmpty()) {
            return new int[]{pos - 1, LEFT};
        } else if (mOne.get(pos - span).isEmpty()) {
            return new int[]{pos - span, UP};
        } else {
            return new int[]{NONE, NONE};
        }
    }

    private int[] rightBottom(int pos) {
        if (mOne.get(pos - 1).isEmpty()) {
            return new int[]{pos - 1, LEFT};
        } else if (mOne.get(pos - span).isEmpty()) {
            return new int[]{pos - span, UP};
        } else {
            return new int[]{NONE, NONE};
        }
    }

}
