package com.jiuzhang.guojing.awesomeresume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.awesomeresume.model.BasicInfo;
import com.jiuzhang.guojing.awesomeresume.model.Education;
import com.jiuzhang.guojing.awesomeresume.model.Experience;
import com.jiuzhang.guojing.awesomeresume.util.DateUtils;
import com.jiuzhang.guojing.awesomeresume.util.ModelUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_EDIT_EDUCATION = 100;
    private static final int REQ_CODE_EDIT_EXPERIENCE = 101;

    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";

    private BasicInfo basicInfo;
    private List<Education> educations;
    private List<Experience> experiences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        setupUI();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_EDIT_EDUCATION:
                    Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                    updateEducation(education);
                    break;
                case REQ_CODE_EDIT_EXPERIENCE:
                    Experience experience = data.getParcelableExtra(ExperienceEditActivity.KEY_EXPERIENCE);
                    updateExperience(experience);
                    break;
            }
        }
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton addEducationBtn = (ImageButton) findViewById(R.id.add_education_btn);
        addEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });

        ImageButton addExperienceBtn = (ImageButton) findViewById(R.id.add_experience_btn);
        addExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });

        setupBasicInfo(basicInfo);
        setupEducations();
        setupExperiences();
    }

    private void setupBasicInfo(BasicInfo basicInfo) {
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }

    private void setupEducations() {
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.educations);
        educationsLayout.removeAllViews();
        for (Education education : educations) {
            View educationView = getLayoutInflater().inflate(R.layout.education_item, null);
            setupEducation(educationView, education);
            educationsLayout.addView(educationView);
        }
    }

    private void setupEducation(View educationView, final Education education) {
        String dateString = DateUtils.dateToString(education.startDate)
                + " ~ " + DateUtils.dateToString(education.endDate);
        ((TextView) educationView.findViewById(R.id.education_school))
                .setText(education.school + " (" + dateString + ")");
        ((TextView) educationView.findViewById(R.id.education_courses))
                .setText(formatItems(education.courses));

        ImageButton editEducationBtn = (ImageButton) educationView.findViewById(R.id.edit_education_btn);
        editEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });
    }

    private void setupExperiences() {
        LinearLayout experiencesLayout = (LinearLayout) findViewById(R.id.experiences);
        experiencesLayout.removeAllViews();
        for (Experience experience : experiences) {
            View experienceView = getLayoutInflater().inflate(R.layout.experience_item, null);
            setupExperience(experienceView, experience);
            experiencesLayout.addView(experienceView);
        }
    }

    private void setupExperience(View experienceView, final Experience experience) {
        String dateString = DateUtils.dateToString(experience.startDate)
                + " ~ " + DateUtils.dateToString(experience.endDate);
        ((TextView) experienceView.findViewById(R.id.experience_company))
                .setText(experience.company + " (" + dateString + ")");
        ((TextView) experienceView.findViewById(R.id.experience_details))
                .setText(formatItems(experience.details));

        ImageButton editExperienceBtn = (ImageButton) experienceView.findViewById(R.id.edit_experience_btn);
        editExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE, experience);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });
    }

    private void loadData() {
        basicInfo = new BasicInfo();
        basicInfo.name = "Jing Guo";
        basicInfo.email = "guojing@jiuzhang.com";

        List<Education> savedEducation = ModelUtils.read(
                this, MODEL_EDUCATIONS, new TypeToken<List<Education>>(){});
        educations = savedEducation == null
                ? new ArrayList<Education>()
                : savedEducation;

        List<Experience> savedExperience = ModelUtils.read(
                this, MODEL_EXPERIENCES, new TypeToken<List<Experience>>(){});
        experiences = savedExperience == null
                ? new ArrayList<Experience>()
                : savedExperience;
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

    private void updateEducation(Education education) {
        boolean found = false;
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (e.school.equals(education.school)) {
                found = true;
                educations.set(i, education);
                break;
            }
        }

        if (!found) {
            educations.add(education);
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);

        setupEducations();
    }

    private void updateExperience(Experience experience) {
        boolean found = false;
        for (int i = 0; i < experiences.size(); ++i) {
            Experience e = experiences.get(i);
            if (e.company.equals(experience.company)) {
                found = true;
                experiences.set(i, experience);
                break;
            }
        }

        if (!found) {
            experiences.add(experience);
        }

        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);

        setupExperiences();
    }
}
