package yool.ma.portfolioservice.ennum;

import java.util.Arrays;
import java.util.List;

public enum MediaType {
    IMAGE(Arrays.asList("image/jpeg", "image/png", "image/gif", "image/svg+xml")),
    DOCUMENT(Arrays.asList("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
    VIDEO(Arrays.asList("video/mp4", "video/quicktime", "video/x-msvideo")),
    PRESENTATION(Arrays.asList("application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation")),
    CODE(Arrays.asList("text/plain", "text/html", "text/css", "application/javascript", "application/json", "text/x-python", "text/x-java"));

    private final List<String> mimeTypes;

    MediaType(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    public static MediaType fromMimeType(String mimeType) {
        for (MediaType type : MediaType.values()) {
            if (type.getMimeTypes().contains(mimeType)) {
                return type;
            }
        }
        return null;
    }
}
