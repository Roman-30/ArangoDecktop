package vsu.csf.arangodbdecktop.model;

public class ResultModel {
    private Object argv1;
    private Object argv2;

    public ResultModel(Object argv1, Object argv2) {
        this.argv1 = argv1;
        this.argv2 = argv2;
    }

    public ResultModel() {
    }

    public Object getArgv1() {
        return argv1;
    }

    public void setArgv1(Object argv1) {
        this.argv1 = argv1;
    }

    public Object getArgv2() {
        return argv2;
    }

    public void setArgv2(Object argv2) {
        this.argv2 = argv2;
    }
}
