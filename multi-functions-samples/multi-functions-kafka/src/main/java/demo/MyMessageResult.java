package demo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class MyMessageResult {
    private String origin;
    private String query;
    private String results;
}
