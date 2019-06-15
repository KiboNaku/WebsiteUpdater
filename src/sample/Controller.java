package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    @FXML
    private ScrollPane centerScrollPane;

    //Basic Info
    @FXML
    private TextField projectName;
    @FXML
    private ComboBox projectStatus;
    @FXML
    private DatePicker dateCompleted;
    @FXML
    private ComboBox projectType;
    @FXML
    private TextArea description;

    //Skills Used
    @FXML
    private TilePane skillsUsed;


    //Additional Links
    @FXML
    private VBox additionalLinks;

    //Submit
    @FXML
    private Button generate;

//    private List<Control> requiredFields;

    private String htmlLocation = "C:\\Users\\nakuk\\OneDrive\\Documents\\GitHub\\KiboNaku.github.io\\index.html";

    @FXML
    public void initialize(){
        dateCompleted.setVisible(false);
        generate.setDisable(true);

//        requiredFields = Arrays.asList(projectName, projectStatus, dateCompleted, projectType, description);
    }

    private static boolean isTextInputFilledIn(TextInputControl textInputControl){
        return textInputControl.getText() != null && textInputControl.getText().trim().length() > 0;
    }

    private static boolean isComboBoxBaseFilledIn(ComboBoxBase comboBoxBase){
        return comboBoxBase.getValue() != null;
    }

    private boolean projectPublished(){
        return projectStatus.getValue().toString().toLowerCase().equals("published");
    }

    private boolean isProjectStatusFilled(){
        if(!isComboBoxBaseFilledIn(projectType)) return false;
        if(projectPublished()) return isComboBoxBaseFilledIn(dateCompleted);
        return true;
    }

    private boolean isSkillsFilledIn(){
        List<Node> skills =  skillsUsed.getChildren();
        for(Node skill : skills){
            CheckBox skillCheckBox = (CheckBox) skill;
            if(skillCheckBox.isSelected()) return true;
        }
        return false;
    }

    private boolean hasAdditionalLinks(){
        List<Node> links = additionalLinks.getChildren();
        for(Node link : links){
            HBox linkHBox = (HBox) link;
            TextField linkField = (TextField) linkHBox.getChildren().get(1);
            if(isTextInputFilledIn(linkField)) return true;
        }
        return false;
    }

    private boolean formFilledOut(){
        return isTextInputFilledIn(projectName) && isComboBoxBaseFilledIn(projectStatus)
                && isProjectStatusFilled() && isTextInputFilledIn(description);
    }

    @FXML
    public void checkStatus(){
        if(projectPublished()){
            dateCompleted.setVisible(true);
        }else{
            dateCompleted.setVisible(false);
        }
        canSubmit();
    }

    @FXML
    public void canSubmit(){
        generate.setDisable(!formFilledOut());
    }

    @FXML
    public void createProject() {

        String html = generateProjectHTML();
        System.out.println(html);
        try {
            updateHtml(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateProjectHTML(){

        String skillsStr = isSkillsFilledIn() ? getSkillsString() : "";
        String linksStr = hasAdditionalLinks() ? getLinksString() : "";
        return  "\n" +
                tab(4) + "<div class=\"project\">\n" +
                    tab(5) + "<h3>" + projectName.getText() + "</h3>\n" +
                    tab(5) +     "<p class=\"type\">" + "Type: " + projectType.getValue().toString() + "</p>\n" +
                    (projectPublished() ?
                        tab(5) + "<p class=\"date\">" + dateCompleted.getValue().toString() + "</p>\n" : "") +
                    tab(5) + "<p>" + description.getText() + "</p>\n" +
                    skillsStr +
                    linksStr +
                tab(4) + "</div>\n";
    }

    private String tab(int tabs){
        return "\t".repeat(tabs);
    }

    private void updateHtml(String projectStr) throws IOException {

        StringBuilder htmlBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(htmlLocation));
            String str;
            while ((str = in.readLine()) != null) {
                htmlBuilder.append(str).append("\n");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fullHtml = htmlBuilder.toString();
//        System.out.println(fullHtml);
        String pattern = "(.*<h1 id=\"projects_heading\">Noteable Projects</h1>).*";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern, Pattern.DOTALL);

        // Now create matcher object.
        Matcher m = r.matcher(fullHtml);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
        } else {
            System.out.println("NO MATCH");
        }

        String end = m.group(0).substring(m.group(1).length());

        BufferedWriter writer = new BufferedWriter(new FileWriter(htmlLocation));
//        System.out.println("beginning: " + m.group(0));
//        System.out.println("end: " + end);
        writer.write(m.group(1) + "\n" +  projectStr + end);
        writer.close();
    }

    private String getSkillsString() {

        List<String> skills = new ArrayList<>();
        for (Node checkBox : skillsUsed.getChildren()) {
            if (((CheckBox) checkBox).isSelected()) skills.add(((CheckBox) checkBox).getText());
        }

        StringBuilder skillBase = new StringBuilder();
        for (String skill : skills)
            skillBase.append(tab(6))
                .append("<li>").append(skill).append("</li>").append("\n");
        return
                "\n" +
                tab(5) + "Skills Used:\n" +
                    tab(5) + "<ul>\n" +
                        skillBase.toString() + "\n" +
                    tab(5) + "</ul>\n";
    }

    private String getLinksString(){
        StringBuilder linksStr = new StringBuilder();

        List<Node> links = additionalLinks.getChildren();
        for(Node link : links){
            List<Node> linkHBox = ((HBox) link).getChildren();
            Label linkLabel = (Label) linkHBox.get(0);
            TextField linkField = (TextField) linkHBox.get(1);
            if(isTextInputFilledIn(linkField)){
                linksStr
                        .append(tab(6))
                        .append("<a href=\"")
                        .append(linkField.getText())
                        .append("\">")
                        .append(linkLabel.getText())
                        .append("</a><br>\n");
            }
        }
        return tab(5) + "<p>\n" + linksStr.toString() + tab(5) + "</p>\n";
    }
}

