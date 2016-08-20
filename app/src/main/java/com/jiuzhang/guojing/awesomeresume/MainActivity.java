package com.jiuzhang.guojing.awesomeresume;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jiuzhang.guojing.awesomeresume.model.BasicInfo;
import com.jiuzhang.guojing.awesomeresume.model.Education;
import com.jiuzhang.guojing.awesomeresume.model.Experience;
import com.jiuzhang.guojing.awesomeresume.model.Project;
import com.jiuzhang.guojing.awesomeresume.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    private BasicInfo basicInfo;
    private Education education;
    private Experience experience;
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fakeData();
        setupUI();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        setupBasicInfo();
        setupEducation();
        setupExperience();
        setupProject();
    }

    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }

    private void setupEducation() {
        String dateString = DateUtils.dateToString(education.startDate)
                + " ~ " + DateUtils.dateToString(education.endDate);
        ((TextView) findViewById(R.id.education_school))
                .setText(education.school + " (" + dateString + ")");
        ((TextView) findViewById(R.id.education_courses))
                .setText(formatItems(education.courses));
    }

    private void setupExperience() {
        String dateString = DateUtils.dateToString(experience.startDate)
                + " ~ " + DateUtils.dateToString(experience.endDate);
        ((TextView) findViewById(R.id.experience_company))
                .setText(experience.company + " (" + dateString + ")");
        ((TextView) findViewById(R.id.experience_details))
                .setText(formatItems(experience.details));
    }

    private void setupProject() {
        String dateString = DateUtils.dateToString(project.startDate)
                + " ~ " + DateUtils.dateToString(project.endDate);
        ((TextView) findViewById(R.id.project_name))
                .setText(project.name + " (" + dateString + ")");
        ((TextView) findViewById(R.id.project_details))
                .setText(formatItems(project.details));
    }

    private void fakeData() {
        basicInfo = new BasicInfo();
        basicInfo.name = "Jing Guo";
        basicInfo.email = "guojing@jiuzhang.com";

        education = new Education();
        education.school = "THU";
        education.major = "Computer Science";
        education.startDate = DateUtils.stringToDate("09/2013");
        education.endDate = DateUtils.stringToDate("09/2015");

        List<String> courses = new ArrayList<>();
        courses.add("Database");
        courses.add("Algorithm");
        courses.add("OO Design");
        courses.add("Operating System");
        education.courses = courses;

        experience = new Experience();
        experience.company = "LinkedIn";
        experience.title = "Software Engineer";
        experience.startDate = DateUtils.stringToDate("09/2015");
        experience.endDate = DateUtils.stringToDate("04/2016");

        List<String> experienceDetails = new ArrayList<>();
        experienceDetails.add("Built something using some tech");
        experienceDetails.add("Built something using some tech");
        experienceDetails.add("Built something using some tech");
        experience.details = experienceDetails;

        project = new Project();
        project.name = "AwesomeResume - an Android resume app";
        project.startDate = DateUtils.stringToDate("10/2015");
        project.endDate = DateUtils.stringToDate("11/2015");

        List<String> projectDetails = new ArrayList<>();
        projectDetails.add("Completed xxx using xxx");
        projectDetails.add("Completed xxx using xxx");
        projectDetails.add("Completed xxx using xxx");
        project.details = projectDetails;
    }

    public static String formatItems(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
