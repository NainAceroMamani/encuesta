package com.nain.encuesta.models;

public class Preguntas {
    private String id;
    private Number preg_01;
    private Number preg_02;
    private Number preg_03;
    private Number preg_04;
    private Number preg_05;
    private Number preg_06;
    private Number preg_07;
    private Number preg_08;
    private String docente_id;
    private String user_id;
    private Number total;

    public Preguntas() {
    }

    public Preguntas(String id, Number preg_01, Number preg_02, Number preg_03, Number preg_04,
                     Number preg_05, Number preg_06, Number preg_07, Number preg_08, Number total, String docente_id, String user_id) {
        this.id = id;
        this.preg_01 = preg_01;
        this.preg_02 = preg_02;
        this.preg_03 = preg_03;
        this.preg_04 = preg_04;
        this.preg_05 = preg_05;
        this.preg_06 = preg_06;
        this.preg_07 = preg_07;
        this.preg_08 = preg_08;
        this.total = total;
        this.docente_id = docente_id;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Number getPreg_01() {
        return preg_01;
    }

    public void setPreg_01(Number preg_01) {
        this.preg_01 = preg_01;
    }

    public Number getPreg_02() {
        return preg_02;
    }

    public void setPreg_02(Number preg_02) {
        this.preg_02 = preg_02;
    }

    public Number getPreg_03() {
        return preg_03;
    }

    public void setPreg_03(Number preg_03) {
        this.preg_03 = preg_03;
    }

    public Number getPreg_04() {
        return preg_04;
    }

    public void setPreg_04(Number preg_04) {
        this.preg_04 = preg_04;
    }

    public Number getPreg_05() {
        return preg_05;
    }

    public void setPreg_05(Number preg_05) {
        this.preg_05 = preg_05;
    }

    public Number getPreg_06() {
        return preg_06;
    }

    public void setPreg_06(Number preg_06) {
        this.preg_06 = preg_06;
    }

    public Number getPreg_07() {
        return preg_07;
    }

    public void setPreg_07(Number preg_07) {
        this.preg_07 = preg_07;
    }

    public Number getPreg_08() {
        return preg_08;
    }

    public void setPreg_08(Number preg_08) {
        this.preg_08 = preg_08;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public String getDocente_id() {
        return docente_id;
    }

    public void setDocente_id(String docente_id) {
        this.docente_id = docente_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
