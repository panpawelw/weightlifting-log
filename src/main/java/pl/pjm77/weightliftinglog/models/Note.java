package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a note. It's either attached to workout as general or to an exercise or a
 * particular set of an exercise. The note has its content and a type.
 *
 * type - type of note - text (0), audio (1), picture (2) or video (3)
 * content - stores content of the text note directly or filename in any other case
 */
public class Note implements Serializable {

    private int type;
    private String content;

    public Note() {}

    public Note(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return getType() == note.getType() &&
                Objects.equals(getContent(), note.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getContent());
    }
}