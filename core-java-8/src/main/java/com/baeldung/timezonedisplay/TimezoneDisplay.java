package com.baeldung.timezonedisplay;

import sun.util.calendar.ZoneInfo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class TimezoneDisplay {

    public enum OffsetBase {
        GMT, UTC
    }

    public List<String> compileListFor(OffsetBase base) {
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();

        LocalDateTime now = LocalDateTime.now();
        return availableZoneIds
          .stream()
          .map(ZoneId::of)
          .sorted(new ZoneComparator())
          .map(id -> String.format("(%s%s) %s", base, getOffset(now, id), id.getId()))
          .collect(Collectors.toList());
    }

    private String getOffset(LocalDateTime dateTime, ZoneId id) {
        return dateTime
          .atZone(id)
          .getOffset()
          .getId()
          .replace("Z", "+00:00");
    }

    private class ZoneComparator implements Comparator<ZoneId> {

        @Override
        public int compare(ZoneId zoneId1, ZoneId zoneId2) {
            TimeZone tz1 = ZoneInfo.getTimeZone(zoneId1);
            TimeZone tz2 = ZoneInfo.getTimeZone(zoneId2);

            return Integer.compare(tz1.getRawOffset(), tz2.getRawOffset());
        }
    }

}
