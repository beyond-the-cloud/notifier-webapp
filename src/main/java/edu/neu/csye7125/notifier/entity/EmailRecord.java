package edu.neu.csye7125.notifier.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "emailRecord",
    indexes = {
        @Index(name = "userIdIndex", columnList = "userId"),
        @Index(name = "userIdAndStoryIdIndex", columnList = "userId, storyId")
    }
)
public class EmailRecord {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userId")
    private String userId;

    @Column(name = "emailAddress")
    private String emailAddress;

    @Column(name = "storyId")
    private String storyId;

    @Column(name = "storyTitle")
    private String storyTitle;

    @Column(name = "sentAt")
    private Timestamp sentAt;

}
