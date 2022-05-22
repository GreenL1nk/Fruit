package greenlink.utils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UUIDUtil {

    private UUIDUtil() {}

    public static List<UUID> load(List<String> uuids) {
        return uuids.stream().map(UUID::fromString).collect(Collectors.toList());
    }

    public static List<String> saveReadable(List<UUID> uuids) {
        return uuids.stream().map(UUID::toString).collect(Collectors.toList());
    }
}
