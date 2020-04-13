package com.example.naucime;

public class Question {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private int answer;
    private int relatedLesson_ID;
    private int id;

    public Question(){

    }

    public Question(String question, String option1, String option2, String option3, int answer, int relatedLesson) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answer = answer;
        this.relatedLesson_ID = relatedLesson;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getRelatedLesson() {
        return relatedLesson_ID;
    }

    public void setRelatedLesson(int relatedLession) {
        this.relatedLesson_ID = relatedLession;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
