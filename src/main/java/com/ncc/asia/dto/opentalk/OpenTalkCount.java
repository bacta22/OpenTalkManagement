package com.ncc.asia.dto.opentalk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OpenTalkCount {
    private Integer year;
    private Long numberOfOpenTalk;

    public  OpenTalkCount(Integer year, Long numberOfOpenTalk) {
        this.year = year;
        this.numberOfOpenTalk = numberOfOpenTalk;
    }

}
