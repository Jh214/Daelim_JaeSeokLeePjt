package jaeseok.jaeseoklee.repository;

import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.entity.schedule.DayOfWeek;
import jaeseok.jaeseoklee.entity.schedule.Period;
import jaeseok.jaeseoklee.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT sh FROM Schedule sh WHERE sh.user.userId = :userId")
    List<Schedule> findByUserId(@Param("userId") String userId);

    boolean existsByUserAndPeriodAndDayOfWeek(User user, Period period, DayOfWeek dayOfWeek);
}
