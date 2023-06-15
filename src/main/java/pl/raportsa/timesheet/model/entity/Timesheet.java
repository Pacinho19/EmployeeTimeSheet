package pl.raportsa.timesheet.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import pl.raportsa.timesheet.model.enums.WorkType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;


@Entity
@Table(name = Timesheet.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class Timesheet {

    public static final String TABLE_NAME ="TIMESHEET";

    @Id
    @GeneratedValue(generator = "GEN_TIMESHEET_ID")
    @GenericGenerator(name = "GEN_TIMESHEET_ID", strategy = "increment")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @Column(updatable = false)
    private LocalDate workDate;

    @Column(updatable = false)
    private String signSrc;

    @Column(updatable = false, length = 1000)
    private String signHashCode;

    @Column(updatable = false)
    private Timestamp signDate;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private WorkType workType;


    public Timesheet(LocalDate workDate) {
        this.workDate=workDate;
    }
}
