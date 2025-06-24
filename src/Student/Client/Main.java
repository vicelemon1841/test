/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Student.Client;

import Student.vo.lecVO;
import Student.vo.stdVO;
import Student.vo.testDTO;
import Student.vo.totalDTO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author 쌍용교육센터
 */
public class Main extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Main.class.getName());

    /**
     * Creates new form page1
     */
    JMenuBar m_bar;
    JMenuItem m_exit;
    JMenu menu;

    //============================
    String data[][];
    String col[];
    List<totalDTO> list;
    List<testDTO> test_list;
    dummyStd dummy;
    stdVO stdvo;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JTable jTable1;
    private JTable jTable2;
    private JButton jButton1;
    public Main() {
//        m_bar = new JMenuBar();
//        menu = new JMenu("로그아웃");
////        m_exit = new JMenuItem("로그아웃");
//
////
////        menu.add(m_exit);
//
//        m_bar.add(menu);
//        this.setJMenuBar(m_bar);
        //======================= 화면 구성
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        //===========================dummyData 생성 =================
        dummy = new dummyStd("3"); //임시 학번: 3 , 삼지매, 010-3333-3333,....



        //==========================================================
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //===========================
        //화면 구성
        initComponents();

        this.setBounds(300,100,600,600);
        this.setVisible(true);

        try {
            search(); // DB에서 값 받아오기
            search2();
            setCard();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터 불러오기 실패: " + e.getMessage());
        }
        //이벤트 감지자
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //테이블1 에서 더블 클릭을 했는지 확인
                int cnt = e.getClickCount();
                if(cnt == 2){
                    //JTable에서 선택된 행, index를 얻어내자.
                    int i = jTable1.getSelectedRow();

                    totalDTO vo = list.get(i); //선택된 행의 vo객체를 뽑아낸다.

                    if(vo != null){
                        System.out.println("vo is not null");
                        try {
                            lec_search(vo); //생성자 파라메터로 선택된 vo객체를 전달해줌.
                            new LecDialog(vo);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    System.out.println("더블클릭 완료");
//                    MyDialog md = new MyDialog(Main.this, true, vo);
                }
            }
        });

        //시험 tab의 테이블 리스너
        jTable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //테이블에서 더블 클릭을 했는지 확인
                int cnt = e.getClickCount();
                if(cnt == 2){
                    //JTable에서 선택된 행, index를 얻어내자.
                    int i = jTable2.getSelectedRow();
                    //setTitle(String.valueOf(i));
                    testDTO vo = test_list.get(i);
                    if(vo != null){
                        System.out.println("test vo is not null");
                    }
                    System.out.println("test table 더블클릭 완료");

                }
            }
        });

        //정보 변경 버튼 리스너
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //변경 버튼을 눌렀을 때 수행
                // 새 다이어그램 띄우기, 정보 수정. (이름, 학번은 고정.)
                if(stdvo != null){
                    ModifyDialog md = new ModifyDialog(stdvo);
                }
            }
        });



    }//생성자의 끝

    public static void main(String args[]) {

        new Main();
    }//Main함수 끝

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        JPanel title = new JPanel();
        JLabel lb_t = new JLabel();
        title.add(lb_t);

        JPanel jPanel1 = new JPanel();
        JTabbedPane jTabbedPane1 = new JTabbedPane();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        JPanel jPanel4 = new JPanel();
        JLabel jLabel2 = new JLabel();
        // Variables declaration - do not modify
        jButton1 = new JButton();
        JPanel jPanel7 = new JPanel();
        JLabel jLabel3 = new JLabel();
        JLabel jLabel4 = new JLabel();
        JLabel jLabel5 = new JLabel();
        JLabel jLabel6 = new JLabel();
        JLabel jLabel7 = new JLabel();
        jLabel1 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        JPanel jPanel5 = new JPanel();
        JScrollPane jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        JPanel jPanel6 = new JPanel();
        JScrollPane jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setLayout(new GridLayout(2, 2));
//        jLabel2.setIcon(new ImageIcon(getClass().getResource("/images/image.png")));
//        jLabel2.setText("jLabel2");

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/image.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(52, 47, Image.SCALE_SMOOTH);
        jLabel2.setIcon(new ImageIcon(scaledImage));
        lb_t.setIcon(new ImageIcon(scaledImage));

        jButton1.setText("정보 변경");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(72, 72, 72)
                                                .addComponent(jButton1)))
                                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(29, 29, 29))
        );

        jLabel3.setText("e-mail :");

        jLabel4.setText("이름 :");

        jLabel5.setText("주소 :");

        jLabel6.setText("학번 :");

        jLabel7.setText("전화번호 :");


        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel11, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel8, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel9, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel10, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addContainerGap(10, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3);

        jTabbedPane1.addTab("학생 카드", jPanel2);


        jScrollPane1.setViewportView(jTable1);

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("강의실", jPanel5);

        jScrollPane2.setViewportView(jTable2);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("시험", jPanel6);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1, GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
        );
        getContentPane().add(title, BorderLayout.NORTH);
        getContentPane().add(jPanel1, BorderLayout.CENTER);


        pack();
    }// </editor-fold>


    //학생 카드를 띄우는 메소드========================================================================
    private void setCard() throws IOException {
        Reader r = Resources.getResourceAsReader("Student/config/conf.xml");

        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(r);
        SqlSession ss = factory.openSession();
        stdvo = ss.selectOne("std.get_no",dummy.getStdno());
        if(stdvo != null) {
            System.out.printf("%s, %s, %s, %s\n", stdvo.getStd_name(), stdvo.getStd_email(), stdvo.getStd_phone(), stdvo.getStd_address());

            //학생카드의 label에 각각 알맞는 값을 지정해서 넣어줌.
            jLabel1.setText("2025 - " + stdvo.getStdno());
            jLabel8.setText(stdvo.getStd_email());
            jLabel9.setText(stdvo.getStd_name());
            jLabel10.setText(stdvo.getStd_phone());
            jLabel11.setText(stdvo.getStd_address());
        }
        ss.close();

    }
    //학생 카드를 띄우는 메소드의 끝=========================================================================


    //table1을 출력하는 메소드============================================================================
    private void search() throws IOException {


        //config와 연결
        Reader r = Resources.getResourceAsReader("Student/config/conf.xml");

        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(r);
        SqlSession ss = factory.openSession();
        //==========================================여기서부터 table1==========================


        list = ss.selectList("std.get_total", dummy.getStdno());

        if (list != null && list.size() > 0) { //DB로부터 받은 data가 있을 때만 수행
            // 받은 list를 JTable에 표현하기 위해 2차원 배열로 만들어야됨.
            col = new String[]{"강의명", "강사명", "개강일", "강의코드"};

            data = new String[list.size()][col.length];
            int i = 0;
            for (totalDTO vo : list) {
                vo = list.get(i);
                data[i][0] = vo.getLec_name(); //강의명 출력
                data[i][1] = vo.getAd_name(); //강사명 들어가야됨
                data[i][2] = vo.getAd_sdate(); // 개강일 들어가야됨
                data[i][3] = vo.getLec_no(); // 강의 번호 들어가야됨
                //임시출력
                System.out.println(vo.getAd_sdate());
                i++;

            }

        } else {
            System.out.println("list = null");

        }
        //JTable갱신
        jTable1.setModel(new DefaultTableModel(data, col)); //setModel 해주고
        jTable1.setDefaultEditor(Object.class, null); //테이블 수정을 막아줘야됨.
        ss.close();
        //==============================여기까지 table1===============================
    }//search함수의 끝
    //tabl1에서 더블클릭 시 정보를 띄우는 메소드==========================================
    private void lec_search(totalDTO vo) throws IOException {
        //임시 출력 확인
        System.out.println(vo.getLec_no());

        //이제 받아온 totalDTO객체의 lec_no를 이용해서 나머지 정보들을 출력해줘야된다.
        Reader r = Resources.getResourceAsReader("Student/config/conf.xml");

        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(r);
        SqlSession ss = factory.openSession();
        List<lecVO> lecVOList = ss.selectList("lec.get_lec", vo.getLec_no());

        if(lecVOList != null){

            for(lecVO vo2 : lecVOList){
                System.out.printf("%s, %s, %s, %s, %s\n",vo2.getAd_name(), vo2.getLec_name(), vo2.getLec_sdate(), vo2.getLec_edate(), vo2.getLec_info());
            }
        }else {
            System.out.println("lec list is null");
        }
    }
    //tabl1에서 더블클릭 시 정보를 띄우는 메소드==========================================


    //table2를 생성하는 메소드=====================================================
    private void search2() throws IOException {
        //config와 연결
        Reader r = Resources.getResourceAsReader("Student/config/conf.xml");

        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(r);
        SqlSession ss = factory.openSession();
        //========================= table2 생성 ==========================
        col = new String[]{"강의코드","강의명", "강사명", "시험 이름", "응시 여부", "시험 보기(임시)"};

        test_list = ss.selectList("std.get_test", dummy.getStdno());

        if (test_list != null && test_list.size() > 0) { //DB로부터 받은 data가 있을 때만 수행
            // 받은 list를 JTable에 표현하기 위해 2차원 배열로 만들어야됨.

            data = new String[test_list.size()][col.length];
            int i = 0;
            for (testDTO vo : test_list) {
                vo = test_list.get(i);
                data[i][0] = vo.getLec_no();// 강의 코드
                data[i][1] = vo.getLec_name(); //강의명 출력
                data[i][2] = vo.getAd_name(); //강사명 들어가야됨
                data[i][3] = vo.getTest_name(); // 시험이름 들어가야됨
                data[i][4] = "응시여부 임시"; //응시여부
                data[i][5] = "임시 data"; //시험 보기? 모르겠다

                i++;

            }

        } else {
            System.out.println("test_list = null");

        }
        //JTable갱신
        jTable2.setModel(new DefaultTableModel(data, col)); //setModel 해주고
        jTable2.setDefaultEditor(Object.class, null); //테이블 수정을 막아줘야됨.
        ss.close();


    }//search2 method의 끝
//==================================================================================


}
// End of variables declaration

