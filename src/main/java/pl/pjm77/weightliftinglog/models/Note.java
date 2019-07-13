package pl.pjm77.weightliftinglog.models;

/**
 * This class represents a note. It's either attached to workout as general or to an exercise or a
 * particular set of an exercise. The note has its content and a type. Type can be text, audio,
 * picture or a video clip. Text note is stored in content directly, while audio, picture or video
 * note has a path to a file stored on server in it's content
 */
public class Note {

    private byte type;
    private String content;
}
