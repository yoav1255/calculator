package org.example;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class PrimaryController {

    @FXML
    private Button a_Button;

    @FXML
    private Button b_Button;

    @FXML
    private Button c_Button;



    @FXML
    private Button d_Button;

    @FXML
    private Button e_Button;

    @FXML
    private Button eight_Button;


    @FXML
    private Button f_Button;

    @FXML
    private Button five_Button;

    @FXML
    private Button four_Button;

    @FXML
    private ComboBox<String> list_box;


    @FXML
    private Button nine_Button;

    @FXML
    private Button seven_Button;

    @FXML
    private Button six_Button;

    @FXML
    private TextField summaryTF;
    @FXML
    private TextField val;

    @FXML
    private Button three_Button;

    @FXML
    private Button two_Button;

    private String expression ="";
    private int baseStart = 0;
    private List numList = new ArrayList<Integer>();
    private List opList = new ArrayList<Character>();
    private int score =0;
    boolean hasOP =false;

    @FXML
    void initialize(){
        list_box.getItems().add("HEX");
        list_box.getItems().add("DEC");
        list_box.getItems().add("OCT");
        list_box.getItems().add("BIN");
        //Make Hex default!!
    }
    void enableHEX(){
        two_Button.setDisable(false);
        three_Button.setDisable(false);
        four_Button.setDisable(false);
        five_Button.setDisable(false);
        six_Button.setDisable(false);
        seven_Button.setDisable(false);
        eight_Button.setDisable(false);
        nine_Button.setDisable(false);
        a_Button.setDisable(false);
        b_Button.setDisable(false);
        c_Button.setDisable(false);
        d_Button.setDisable(false);
        e_Button.setDisable(false);
        f_Button.setDisable(false);
    }
    void enableDEC(){
        enableHEX();
        a_Button.setDisable(true);
        b_Button.setDisable(true);
        c_Button.setDisable(true);
        d_Button.setDisable(true);
        e_Button.setDisable(true);
        f_Button.setDisable(true);
    }
    void enableOCTA(){
        enableDEC();
        eight_Button.setDisable(true);
        nine_Button.setDisable(true);
    }
    void enableBIN(){
        enableOCTA();
        two_Button.setDisable(true);
        three_Button.setDisable(true);
        four_Button.setDisable(true);
        five_Button.setDisable(true);
        six_Button.setDisable(true);
        seven_Button.setDisable(true);
    }
    @FXML
    void chooseFromList(ActionEvent event){ //choose and set buttons to disable/enable

        String chosen = list_box.getSelectionModel().getSelectedItem();
        int base = 10;
        switch (chosen) {
            case "HEX":
                base = 16;
                enableHEX();
                break;
            case "DEC":
                base = 10;
                enableDEC();
                break;
            case "OCT":
                base = 8;
                enableOCTA();
                break;
            case "BIN":
                base = 2;
                enableBIN();
                break;
        }
        if(!expression.isEmpty()){
            if(baseStart!=0)
                reCalculateExpression(base);
        }
        baseStart=base;
    }

    void reCalculateExpression(int baseTo){
        String textInVal = val.getText();
        sum();
        expression="";
        int summary = score;
        String s = "";
        String n1="";
        for (int i=0;i<numList.size();i++){
            int num = (int)numList.get(i);

            if(baseTo==2) {
                n1 = Integer.toBinaryString(num);
                s = Integer.toBinaryString(summary);
            }
            if(baseTo==8){
                n1 = Integer.toOctalString(num);
                s = Integer.toOctalString(summary);
            }
            if(baseTo==10){
                n1 = Integer.toString(num);
                s = Integer.toString(summary);
            }
            if(baseTo==16){
                n1 = Integer.toHexString(num).toUpperCase();
                s = Integer.toHexString(summary).toUpperCase();
            }
            expression += n1;
            if(i<numList.size()-1) expression += opList.get(i);
        }
        if(!hasOP) val.setText("");
        else {
            if(textInVal.equals("ERROR"))
                val.setText("ERROR");
            else
                val.setText(s);
        }
        writeArithmetic();
    }
    public static int toDecimal(int base, String sNum) {
        int len = sNum.length();
        int index = len;
        int num;
        int sum = 0;
        while (!sNum.isEmpty()) {
            char c = sNum.charAt(index - 1);
            if (base == 2 && (c < '0' || c > '1')) return -1;
            if (base == 8 && (c < '0' || c > '7')) return -1;
            if (base == 10 && (c < '0' || c > '9')) return -1;
            if (c == 'A') num = 10;
            else if (c == 'B') num = 11;
            else if (c == 'C') num = 12;
            else if (c == 'D') num = 13;
            else if (c == 'E') num = 14;
            else if (c == 'F') num = 15;
            else if (c >= '0' && c <= '9') num = (int) c - 48;
            else return -1;
            sum += num * java.lang.Math.pow(base, len - index);
            index--;
            sNum = sNum.substring(0, index);
        }
        return sum;
    }
    @FXML
    void clearTextArea(ActionEvent event){
        expression="";
        val.setText("");
        numList.clear();
        opList.clear();
        writeArithmetic();
    }
    @FXML void writeArithmetic(){ summaryTF.setText(expression); }
    @FXML void sum(){
        numList.clear();
        opList.clear();
        boolean checkExp = true;
        boolean checkDivByZero = false;
        char c1 = expression.charAt(0);
        char c2 = expression.charAt(expression.length()-1);
        if(c1=='+' || c1=='-' || c1=='*' || c1=='/' || c2=='+' || c2=='-' || c2=='*' || c2=='/') { //first/last char is an op
            checkExp = false;
        }
        String chosen = list_box.getSelectionModel().getSelectedItem();
        if(chosen!=null) {
            int base = 10;
            if (baseStart != 0) base = baseStart;
            else {
                if (chosen.equals("HEX")) base = 16;
                if (chosen.equals("DEC")) base = 10;
                if (chosen.equals("OCT")) base = 8;
                if (chosen.equals("BIN")) base = 2;
            }

            String expression2 = expression;
            String[] arr2 = expression2.split("[-+*/]");
            expression2 = expression;
            for (int i = 0; i < expression2.length(); i++) {
                char c = expression2.charAt(i);
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    opList.add(c);
                    if (i < expression2.length() - 1) {
                        c = expression2.charAt(i + 1);
                        if (c == '+' || c == '-' || c == '*' || c == '/') { //another op right after
                            checkExp = false;
                        }
                    }
                }
            }
            if(!opList.isEmpty()) hasOP = true;
            for (String sNum : arr2) {
                int num = toDecimal(base, sNum);
                numList.add(num);
            }
            // At this point numList contains all the numbers in decimal and opList contains all the ops
            List numTempList = new ArrayList<>(numList);
            if (checkExp) {
                for (int j = 0; j < opList.size(); j++) {
                    char op1 = (char) opList.get(j);
                    if (op1 == '*') {
                        numTempList.set(j + 1, (int) numTempList.get(j) * (int) numTempList.get(j + 1));
                        numTempList.set(j, 0);
                    }
                    if (op1 == '/') {
                        if ((int) numTempList.get(j + 1) == 0) {
                            checkDivByZero = true;
                        }
                        else {
                            numTempList.set(j + 1, (int) numTempList.get(j) / (int) numTempList.get(j + 1));
                            numTempList.set(j, 0);
                        }
                    }
                }
                if(!checkDivByZero) {
                    for (int j = 0; j < opList.size(); j++) {
                        char op1 = (char) opList.get(j);
                        if (op1 == '+') {
                            int index = j;
                            while ((int) numTempList.get(j + 1) == 0)
                                j++;
                            numTempList.set(j + 1, (int) numTempList.get(index) + (int) numTempList.get(j + 1));
                        }
                        if (op1 == '-') {
                            int index = j;
                            while ((int) numTempList.get(j + 1) == 0)
                                j++;
                            numTempList.set(j + 1, (int) numTempList.get(index) - (int) numTempList.get(j + 1));
                        }
                    }

                    score = (int) numTempList.get(opList.size());
                    if (base == 16) val.setText(Integer.toHexString(score).toUpperCase());
                    if (base == 10) val.setText(Integer.toString(score));
                    if (base == 8) val.setText(Integer.toOctalString(score));
                    if (base == 2) val.setText(Integer.toBinaryString(score));
                }
                else {
                    val.setText(("ERROR"));
                    score = -1;
                }
            } else {
                val.setText("ERROR");
                score = -1;
            }
        }else
            val.setText("Choose base!");

    }
    @FXML void keyPressed(KeyEvent event){
        if(baseStart>=2) {
            switch (event.getCode()){
                case DIGIT0:
                case NUMPAD0:
                    expression+='0'; writeArithmetic(); break;
                case DIGIT1:
                case NUMPAD1:
                    expression+='1'; writeArithmetic(); break;

                case PLUS:
                case ADD:
                    expression+='+'; writeArithmetic(); break;
                case MINUS:
                case SUBTRACT:
                    expression+='-'; writeArithmetic(); break;
                case MULTIPLY: expression+='*'; writeArithmetic(); break;
                case DIVIDE: expression+='/'; writeArithmetic(); break;
                case ENTER:
                case EQUALS: sum(); break;
            }
        }
        if(baseStart>=8){
            switch (event.getCode()){
                case DIGIT7:
                case NUMPAD7:
                    expression+='7'; writeArithmetic(); break;
                case DIGIT6:
                case NUMPAD6:
                    expression+='6'; writeArithmetic(); break;
                case DIGIT5:
                case NUMPAD5:
                    expression+='5'; writeArithmetic(); break;
                case DIGIT4:
                case NUMPAD4:
                    expression+='4'; writeArithmetic(); break;
                case DIGIT3:
                case NUMPAD3:
                    expression+='3'; writeArithmetic(); break;
                case DIGIT2:
                case NUMPAD2:
                    expression+='2'; writeArithmetic(); break;
            }
        }
        if(baseStart>=10){
            switch (event.getCode()){
                case DIGIT9:

                case NUMPAD9:
                    expression+='9'; writeArithmetic(); break;
                case DIGIT8:
                case NUMPAD8:
                    expression+='8'; writeArithmetic(); break;
            }
        }
        if(baseStart==16){
            switch (event.getCode()){
                case A: expression+='A'; writeArithmetic(); break;
                case B: expression+='B'; writeArithmetic(); break;
                case C: expression+='C'; writeArithmetic(); break;
                case D: expression+='D'; writeArithmetic(); break;
                case E: expression+='E'; writeArithmetic(); break;
                case F: expression+='F'; writeArithmetic(); break;
            }
        }
    }

    @FXML void add_zero(ActionEvent event){ expression+= '0'; writeArithmetic();}
    @FXML void add_one(ActionEvent event){ expression+= '1'; writeArithmetic();}
    @FXML void add_two(ActionEvent event){ expression+= '2'; writeArithmetic();}
    @FXML void add_three(ActionEvent event){ expression+= '3'; writeArithmetic();}
    @FXML void add_four(ActionEvent event){ expression+= '4'; writeArithmetic();}
    @FXML void add_five(ActionEvent event){ expression+= '5'; writeArithmetic();}
    @FXML void add_six(ActionEvent event){ expression+= '6'; writeArithmetic();}
    @FXML void add_seven(ActionEvent event){ expression+= '7'; writeArithmetic();}
    @FXML void add_eight(ActionEvent event){ expression+= '8'; writeArithmetic();}
    @FXML void add_nine(ActionEvent event){ expression+= '9'; writeArithmetic();}
    @FXML void add_A(ActionEvent event){ expression+= 'A'; writeArithmetic();}
    @FXML void add_B(ActionEvent event){ expression+= 'B'; writeArithmetic();}
    @FXML void add_C(ActionEvent event){ expression+= 'C'; writeArithmetic();}
    @FXML void add_D(ActionEvent event){ expression+= 'D'; writeArithmetic();}
    @FXML void add_E(ActionEvent event){ expression+= 'E'; writeArithmetic();}
    @FXML void add_F(ActionEvent event){ expression+= 'F'; writeArithmetic();}
    @FXML void add_plus(ActionEvent event){ expression+= '+'; writeArithmetic();}
    @FXML void add_minus(ActionEvent event){ expression+= '-'; writeArithmetic();}
    @FXML void add_mul(ActionEvent event){ expression+= '*'; writeArithmetic();}
    @FXML void add_div(ActionEvent event){ expression+= '/'; writeArithmetic();}

}

