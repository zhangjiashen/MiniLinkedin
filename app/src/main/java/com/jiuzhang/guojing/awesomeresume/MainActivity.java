package com.jiuzhang.guojing.awesomeresume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    private static final int REQ_CODE_EDUCATION_EDIT = 100;

    private BasicInfo basicInfo;
    private List<Education> educations;
    private Experience experience;
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fakeData();
        setupUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_EDUCATION_EDIT && resultCode == Activity.RESULT_OK) {
            Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
            updateEducation(education);
        }
    }

    private void updateEducation(Education education) {
        boolean found = false;
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(education.id, e.id)) {
                educations.set(i, education);
                found = true;
                break;
            }
        }

        if (!found) {
            educations.add(education);
        }

        setupEducations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        ImageButton addEducationBtn = (ImageButton) findViewById(R.id.add_education_btn);
        addEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
            }
        });

        ImageButton addExperienceBtn = (ImageButton) findViewById(R.id.add_experience_btn);
        addExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                startActivity(intent);
            }
        });

        ImageButton addProjectBtn = (ImageButton) findViewById(R.id.add_project_btn);
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                startActivity(intent);
            }
        });

        setupBasicInfo();
        setupEducations();
        setupExperience();
        setupProject();
    }

    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }

    private void setupEducations() {
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.educations);
        educationsLayout.removeAllViews();
        for (Education education : educations) {
            educationsLayout.addView(getEducationView(education));
        }
    }

    private View getEducationView(final Education education) {
        View view = getLayoutInflater().inflate(R.layout.education_item, null);
        String dateString = DateUtils.dateToString(education.startDate)
                + " ~ " + DateUtils.dateToString(education.endDate);
        ((TextView) view.findViewById(R.id.education_school))
                .setText(education.school + " (" + dateString + ")");
        ((TextView) view.findViewById(R.id.education_courses))
                .setText(formatItems(education.courses));
        view.findViewById(R.id.edit_education_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
            }
        });
        return view;
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

        Education education1 = new Education();
        education1.school = "THU";
        education1.major = "Computer Science";
        education1.startDate = DateUtils.stringToDate("09/2013");
        education1.endDate = DateUtils.stringToDate("09/2015");

        education1.courses = new ArrayList<>();
        education1.courses.add("Database");
        education1.courses.add("Algorithm");
        education1.courses.add("OO Design");
        education1.courses.add("Operating System");

        Education education2 = new Education();
        education2.school = "CMU";
        education2.major = "Computer Science";
        education2.startDate = DateUtils.stringToDate("09/2015");
        education2.endDate = DateUtils.stringToDate("09/2018");

        education2.courses = new ArrayList<>();
        education2.courses.add("course 1");
        education2.courses.add("course 2");

        educations = new ArrayList<>();
        educations.add(education1);
        educations.add(education2);

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
