package Student.Client;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
import Student.vo.stdVO;
import Student.vo.totalDTO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author 쌍용교육센터
 */
public class MyDialog extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MyDialog.class.getName());

    /**
     * Creates new form MyDialog
     */
    Main parent;
    public  MyDialog(Main parent, boolean modal, String str){
        super(parent, modal);
        this.parent = parent;
        this.setTitle(str);

        initComponents(); //화면구성
        jButton1.setText(str);
        empno_tf.setEditable(true);//활성화
        jButton1.addActionListener(new ActionListener() {
            //저장 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = e.getActionCommand(); //파라메터의 str이 계속 유지될지 불분명하므로 새로 해줌

            }
        });


        this.setVisible(true);
    }

    public MyDialog(Main parent, boolean modal, stdVO vo) {  //modal: 창이 하나 띄워지면 다른거 클릭하더라도 인식 안되게 막는 것임
        super(parent, modal);
        this.parent = parent;


        initComponents(); //화면구성
//        empno_tf.setText(vo.getEmpno()); //사번이 들어감
//        ename_tf.setText(vo.getEname()); //이름이 들어감
//        job_tf.setText(vo.getJob());
//        hdate_tf.setText(vo.getHiredate());
//        sal_tf.setText(vo.getSal());
//        comm_tf.setText(vo.getComm());
//        dname_tf.setText(vo.getDname());
        //setVosible이 main함수에 있었어서 추가해줌

        this.setBounds(300,300,500,500);
        this.setVisible(true);


        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //취소 버튼
                dispose();//현재 창 객체를 메모리 상에서 삭제
            }
        });


        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //저장버튼
                String empno = empno_tf.getText().trim();
                String ename = ename_tf.getText().trim();
                String job = job_tf.getText().trim();
                String hdate = hdate_tf.getText().trim();
                String sal = sal_tf.getText().trim();
                String dname = dname_tf.getText().trim();
                String comm = comm_tf.getText().trim();
                if(comm.length() < 1){
                    comm = null; //또는 "0"
                }

//                수정된 data들 set해주기.
//                EmpVO vo = new EmpVO();
//                vo.setEmpno(empno);
//                vo.setEname(ename);
//                vo.setJob(job);
//                vo.setHiredate(hdate);
//                vo.setSal(sal);
//                vo.setComm(comm);
//                vo.setDname(dname);
//                parent.updateData(vo); //여기서 parent는 EmpFrame 객체, EmpFrame의 함수를 호출함
                dispose(); //저장누르면 화면 종료
            }
        });
        setVisible(true);
    }

    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        empno_tf = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ename_tf = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        job_tf = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        hdate_tf = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        sal_tf = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        comm_tf = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        dname_tf = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(6, 1));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel1.setText("이름:");
        jPanel1.add(jLabel1);

        empno_tf.setEditable(false);
        empno_tf.setColumns(6);
        jPanel1.add(empno_tf);

        getContentPane().add(jPanel1);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel2.setText("학번: ");
        jPanel2.add(jLabel2);

        ename_tf.setColumns(6);
        jPanel2.add(ename_tf);

        getContentPane().add(jPanel2);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel3.setText("e-mail: ");
        jPanel3.add(jLabel3);

        job_tf.setColumns(6);
        jPanel3.add(job_tf);

        getContentPane().add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel4.setText("전화번호: ");
        jPanel4.add(jLabel4);

        hdate_tf.setColumns(6);
        jPanel4.add(hdate_tf);

        getContentPane().add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel5.setText("주소: ");
        jPanel5.add(jLabel5);

        sal_tf.setColumns(6);
        jPanel5.add(sal_tf);

        getContentPane().add(jPanel5);

        jButton1.setText("저장");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton1);

        jButton2.setText("취소");
        jPanel8.add(jButton2);

        getContentPane().add(jPanel8);

        pack();

    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField comm_tf;
    private javax.swing.JTextField dname_tf;
    private javax.swing.JTextField empno_tf;
    private javax.swing.JTextField ename_tf;
    private javax.swing.JTextField hdate_tf;
    private javax.swing.JButton jButton1; //저장 버튼
    private javax.swing.JButton jButton2; //취소 버튼
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField job_tf;
    private javax.swing.JTextField sal_tf;
    // End of variables declaration//GEN-END:variables

}

