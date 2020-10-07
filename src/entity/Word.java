package entity;

public class Word {
    private String word_target;
    private String word_explain;

    public Word() {
    }

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public String getInfor() {
        return this.word_target + " | " + this.word_explain;
    }

    public String toString() {
        String[] rs = this.word_explain.split("\\-");
        String text = rs[0];
        for (int i = 1; i < rs.length; i++) {
            text += "\n-" + rs[i];
        }
        return text;
    }


}
