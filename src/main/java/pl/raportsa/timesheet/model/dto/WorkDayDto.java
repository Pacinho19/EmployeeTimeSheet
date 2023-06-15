package pl.raportsa.timesheet.model.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class WorkDayDto extends WorkDay {

    @Getter
    private Map<String, Work> signSrcMap;

    public WorkDayDto() {
        signSrcMap = new HashMap<>();
    }

    public void addWork(String login, Work work) {
        signSrcMap.put(login, work);
    }

    public Work getWork(String login) {
        return signSrcMap.get(login);
    }
}
