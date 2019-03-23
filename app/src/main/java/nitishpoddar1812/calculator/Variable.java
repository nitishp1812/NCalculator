package nitishpoddar1812.calculator;

class Variable {

    private long id;
    private String variable;
    private String value;

    Variable() {
    }

    Variable(long id, String variable, String value) {
        this.id = id;
        this.variable = variable;
        this.value = value;
    }

    Variable(String variable, String value) {
        this.variable = variable;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    String getVariable() {
        return variable;
    }

    void setVariable(String variable) {
        this.variable = variable;
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }
}
