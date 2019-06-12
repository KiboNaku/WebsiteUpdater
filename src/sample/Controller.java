package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    @FXML
    private TextField projectName;
    @FXML
    private ComboBox projectType;
    @FXML
    private DatePicker dateCompleted;
    @FXML
    private GridPane skillsUsed;
    @FXML
    private TextField githubPage;
    @FXML
    private TextField googlePlay;
    @FXML
    private TextField learnMore;
    @FXML
    private TextArea description;

    private String htmlLocation = "C:\\Users\nakuk\\OneDrive\\Documents\\GitHub\\index.html";

    @FXML
    public void onButtonClicked() {
        String projectNameStr = projectName.getText();
        String projectTypeStr = projectType.getValue().toString();
        String descriptionStr = description.getText();
        String dateCompletedStr = dateCompleted.getValue().toString();

        List<Node> skillsUsedChildren = skillsUsed.getChildren();
        List<String> skillsUsedList = new ArrayList<>();

        for (Node checkBox : skillsUsedChildren) {
            if (((CheckBox) checkBox).isSelected()) skillsUsedList.add(((CheckBox) checkBox).getText());
        }

        String githubLink = githubPage.getText();
        String gPlayLink = googlePlay.getText();
        String learnMoreLink = learnMore.getText();

        String html = getProjectSetup(projectNameStr, projectTypeStr, descriptionStr,
                dateCompletedStr, skillsUsedList, githubLink, gPlayLink, learnMoreLink);
        System.out.println(html);
        try {
            updateHtml(html);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateHtml(String projectStr) throws IOException {

        StringBuilder htmlBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    "C:\\Users\\nakuk\\OneDrive\\Documents\\GitHub\\KiboNaku.github.io\\index.html"));
            String str;
            while ((str = in.readLine()) != null) {
                htmlBuilder.append(str).append("\n");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String html = htmlBuilder.toString();

        String pattern = "(.*<h1 id=\"projects_heading\">Noteable Projects</h1>).*";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern, Pattern.DOTALL);

        // Now create matcher object.
        Matcher m = r.matcher(html);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
        } else {
            System.out.println("NO MATCH");
        }

        String end = m.group(0).substring(m.group(1).length());

        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "C:\\Users\\nakuk\\OneDrive\\Documents\\GitHub\\KiboNaku.github.io\\index.html"));
        System.out.println("beginning: " + m.group(0));
        System.out.println("end: " + end);
        writer.write(m.group(1) + projectStr + end);
        writer.close();
    }

    private String getProjectSetup(String projectName, String type, String descrip, String date,
                                   List<String> skills, String githubLink,
                                   String gPlayLink, String learnMoreLink) {

        String html = "<div class=\"project\">\n" +
                "<h3>" + projectName + "</h3>\n" +
                "<p class=\"type\">Type: " + type + "</p>\n" +
                "<p class=\"date\">" + date + "</p>\n" +
                "<p>" + descrip + "</p>\n";
        if (skills.size() > 0) html += getSkillsString(skills);
        if (githubLink.length() > 0 || gPlayLink.length() > 0 || learnMoreLink.length() > 0) {
            html += "<p>" +
                    (githubLink.length() > 0 ? "<a href=\"" + githubLink + "\">Github page</a><br>" : "") +
                    (gPlayLink.length() > 0 ? "<a href=\"" + gPlayLink + "\">Google Play</a><br>" : "") +
                    (learnMoreLink.length() > 0 ? "<a href=\"" + learnMoreLink + "\">Learn more...</a><br>" : "") +
                    "</p>";
        }
        html += "</div>";
        return html;
    }

    private String getSkillsString(List<String> skills) {
        String skillBase = "";
        for (String skill : skills) skillBase += "<li>" + skill + "</li>";
        String skillsStr = "Skills Used:\n" + "<ul>\n" + skillBase + "\n" + "</ul>";
        return skillsStr;
    }
}
