package jaeseok.jaeseoklee.repository.student;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jaeseok.jaeseoklee.dto.student.StudentFilterDto;
import jaeseok.jaeseoklee.entity.Grade;
import jaeseok.jaeseoklee.entity.student.QStudent;
import jaeseok.jaeseoklee.entity.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jaeseok.jaeseoklee.entity.student.QStudent.student;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Student> findStudentsByUserIdAndGrade(StudentFilterDto studentFilterDto, Pageable pageable) {
        QStudent student = QStudent.student;

        BooleanExpression userIdFilter = student.user.userId.eq(studentFilterDto.getUserId());
//        BooleanExpression gradeFilter = student.studentGrade.eq(studentFilterDto.getStudentGrade());
//        BooleanExpression classNumFilter = student.classNum.eq(studentFilterDto.getClassNum());
        Grade studentGrade = studentFilterDto.getStudentGrade();
        int classNum = studentFilterDto.getClassNum();

        List<Student> students = queryFactory
                .selectFrom(student)
                .where(userIdFilter, studentGradeFilter(studentGrade), classNumFilter(classNum))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(student)
                .where(userIdFilter, studentGradeFilter(studentGrade), classNumFilter(classNum))
                .fetchCount();

        return new PageImpl<>(students, pageable, total);
    }
    public BooleanExpression studentGradeFilter(Grade studentGrade) {
        if (studentGrade == null || studentGrade.equals("")) {
            return null;
        }
            return student.studentGrade.eq(studentGrade);
    }
    public BooleanExpression classNumFilter(int classNum) {
        if (classNum == 0 || classNum < 0) {
            return null;
        }
            return student.classNum.eq(classNum);
    }
}
