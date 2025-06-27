/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Student.Client;

import Student.vo.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;


/**
 * @author 쌍용교육센터
 */
public class StudentFrame extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentFrame.class.getName());

    /**
     * Creates new form page1
     */

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
    private JLabel jLabel_birth;

    private JTable jTable1;
    private JTable jTable2;
    private JButton jButton1; //사진 변경 버튼
    private JButton jButton4; //정보 변경 버튼
    private JLabel jLabel2; //사용자의 프로필 사진

    JButton logout_bt, logout_bt2, logout_bt3;
    JButton exit_bt, exit_bt2, exit_bt3;
    JButton image_bt;
    JLabel lb_t;
    JPanel title;

    Reader r;
    SqlSessionFactory factory;

    public StudentFrame(String stdno) throws IOException {

        r = Resources.getResourceAsReader("Student/config/conf.xml");
        factory = new SqlSessionFactoryBuilder().build(r);
        r.close();
        //======================= 화면 구성
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
//        // 테마 설정 가장 먼저
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //===========================dummyData 생성 =================
        dummy = new dummyStd(stdno); //임시 학번: 3 , 삼지매, 010-3333-3333,....
        // 로그인 -> member_t에서 stdno 가져와야됨 (생성자 파라메터로 넘겨주기?)


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

        this.setBounds(300, 100, 900, 700);
        this.setVisible(true);

        try {
            search(); // DB에서 값 받아오기
            search2();
            set_card();
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
                if (cnt == 2) {
                    //JTable에서 선택된 행, index를 얻어내자.
                    int i = jTable1.getSelectedRow();

                    totalDTO vo = list.get(i); //선택된 행의 vo객체를 뽑아낸다.

                    if (vo != null) {
                        try {
                            lec_search(vo); //생성자 파라메터로 선택된 vo객체를 전달해줌.
                            new LecDialog(vo);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        //시험 tab의 테이블 리스너
        jTable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //테이블에서 더블 클릭을 했는지 확인
                int cnt = e.getClickCount();
                if (cnt == 2) {
                    //JTable에서 선택된 행, index를 얻어내자.
                    int i = jTable2.getSelectedRow();
                    //setTitle(String.valueOf(i));
                    testDTO vo = test_list.get(i);
                    if (vo != null) {
                        System.out.println("test vo is not null");
                    }
                    System.out.println("test table 더블클릭 완료");

                }
            }
        });

        //정보 변경 버튼 리스너
        jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //변경 버튼을 눌렀을 때 수행
                // 새 다이어그램 띄우기, 정보 수정. (이름, 학번은 고정.)
                if (stdvo != null) {
                    ModifyDialog md = new ModifyDialog(stdvo, StudentFrame.this);
                }
            }
        });
        //사진 변경 버튼 리스너
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //사진 변경 작업 수행
                //Filestream이용
                // jLabel2는 프로필 사진 (기본은 empty로 설정, 나머지는 고르기)
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // destFile 경로를 현재 main파일 기준 상대경로로 해주기 (images 패키지로 복사되게끔)
                    File destFile = new File("src/images/" + selectedFile.getName());
                    System.out.println(selectedFile.getName()); //임시 출력
                    
                    if(destFile == null){
                        System.out.println("file is null, 경로 제대로 구현안됨");
                    }
                    try {
                        Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("파일 복사 완료!"); //임시 출력
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    //

                    try (FileInputStream fis = new FileInputStream(destFile)) {
                        // 이미지 읽고 byte[]에 저장
                        byte[] imageBytes = fis.readAllBytes();
                        ImageIcon icon = new ImageIcon(imageBytes);
                        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

                        // JLabel에 설정
                        jLabel2.setIcon(new ImageIcon(scaledImage));

                        // image 수정 메소드 호출
                        update_image(destFile.getAbsolutePath());

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "이미지 불러오기 실패");
                    }
                }
            }
        });


        // 시험 응시 버튼 리스너
        jTable2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = jTable2.rowAtPoint(e.getPoint());
                int col = jTable2.columnAtPoint(e.getPoint());

                if (col == 5) { // 버튼 열
                    String cellValue = jTable2.getValueAt(row, col).toString();

                    if ("시험 응시".equals(cellValue)) {
                        // 응시 버튼이 활성화 된 경우에만 클릭할 수 있게 처리, 새 창 띄우기
                        String test_name = jTable2.getValueAt(row, 3).toString();
                        System.out.println("시험 이름 = " + test_name);
                        
                        // 시험 응시 시작
                        // 시험 응시 창 띄우기 -> dialog 내에서 table 초기화
                    }
                }
            }
        });
        // 시험 결과 확인 버튼 리스너
        jTable2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = jTable2.rowAtPoint(e.getPoint());
                int col = jTable2.columnAtPoint(e.getPoint());

                if (col == 4) { // 버튼 열
                    String cellValue = jTable2.getValueAt(row, col).toString();

                    if ("응시 완료(결과 확인)".equals(cellValue)) {
                        // 응시 버튼이 활성화 된 경우에만 클릭할 수 있게 처리, 새 창 띄우기
                        String test_name = jTable2.getValueAt(row, 3).toString();
                        System.out.println("시험 이름 = " + test_name);
                        JOptionPane.showMessageDialog(StudentFrame.this, test_name + " 시험 응시 완료");
                        // 시험 결과 창 띄우기

                        //....

                        //
                    }
                }
            }
        });

        // 종료, 로그아웃 리스너 공통 정의
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        ActionListener logoutListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("로그아웃");
                // 여기서 로그인 화면으로 전환


                //
            }
        };

        // 버튼 여러 개에 리스너 한번에 적용
        JButton[] exitButtons = {exit_bt, exit_bt2, exit_bt3};
        JButton[] logoutButtons = {logout_bt, logout_bt2, logout_bt3};

        for (JButton b : exitButtons) b.addActionListener(exitListener);
        for (JButton b : logoutButtons) b.addActionListener(logoutListener);


    }//생성자의 끝

    public static void main(String args[]) throws IOException {

        new StudentFrame("3");

    }//Main함수 끝

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")


    // 탭마다 넣을 버튼 패널을 생성하는 메소드 =========================================================
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(logout_bt, BorderLayout.WEST);
        panel.add(exit_bt, BorderLayout.EAST);
        return panel;
    }
    // 탭마다 넣을 버튼 패널을 생성하는 메소드의 끝 =========================================================

    //학생 카드를 띄우는 메소드========================================================================
    private void set_card() throws IOException {

        SqlSession ss = factory.openSession();
        stdvo = ss.selectOne("std.get_no", dummy.getStdno());
        if (stdvo != null) {

            //학생카드의 label에 각각 알맞는 값을 지정해서 넣어줌.
            jLabel1.setText("2025 - " + stdvo.getStdno());
            jLabel1.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel8.setText(stdvo.getStd_email());
            jLabel8.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel9.setText(stdvo.getStd_name());
            jLabel9.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel10.setText(stdvo.getStd_phone());
            jLabel10.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel11.setText(stdvo.getStd_address());
            jLabel11.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel_birth.setText("");
            jLabel_birth.setFont(new Font("맑은 고딕", Font.BOLD, 15));


            //프로필 사진

            if(stdvo.getStd_image() == null){
                ImageIcon emptyIcon = new ImageIcon(getClass().getResource("/images/empty.png"));
                Image scale = emptyIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                jLabel2.setIcon(new ImageIcon(scale));
            } else {
                ImageIcon profile_img = new ImageIcon(getClass().getResource(stdvo.getStd_image()));
                Image scale = profile_img.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                jLabel2.setIcon(new ImageIcon(scale));
            }
        }
        lb_t.setText(stdvo.getStd_name() + "님의 마이페이지");
        lb_t.setFont(new Font("맑은 고딕", Font.BOLD, 25));

        ss.close();
    }
    //학생 카드를 띄우는 메소드의 끝=========================================================================

    //학생 프로필 사진 수정 메소드========================================================================== 수정해야됨
    private void update_image(String str) throws IOException {

        SqlSession ss = factory.openSession();

        int idx = str.indexOf("\\images");
        if (idx != -1) {
            String shortPath = str.substring(idx); // → images\profile1.png
            shortPath = shortPath.replace("\\","/");
            stdvo.setStd_image(shortPath);
            System.out.println("img 경로: " + shortPath);
        } else {
            System.out.println("images\\가 경로에 없습니다.");
        }

        ss.update("std.update_img", stdvo);
        ss.commit();

        ss.close();
    }
    //학생 프로필 사진 수정 메소드==========================================================================

    //학생 카드 수정 메소드 ===============================================================================
    public void update_card(stdVO vo){

        if (vo != null) {

            //학생카드의 label에 각각 알맞는 값을 지정해서 넣어줌.
            jLabel1.setText("2025 - " + vo.getStdno());
            jLabel1.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel8.setText(vo.getStd_email());
            jLabel8.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel9.setText(vo.getStd_name());
            jLabel9.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel10.setText(vo.getStd_phone());
            jLabel10.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            jLabel11.setText(vo.getStd_address());
            jLabel11.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        }
    }

    //학생 카드 수정 메소드 ===============================================================================


    //table1을 출력하는 메소드============================================================================
    private void search() throws IOException {
        
        //==========================================여기서부터 table1==========================
        SqlSession ss = factory.openSession();
        
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
//                System.out.println(vo.getAd_sdate());
                i++;

            }

        } else {
            System.out.println("list = null");

        }
        //JTable갱신
        jTable1.setRowHeight(30); // 테이블 셀 크기
        jTable1.setModel(new DefaultTableModel(data, col)); //setModel 해주고
        jTable1.setDefaultEditor(Object.class, null); //테이블 수정을 막아줘야됨.
        r.close();
        ss.close();
        //==============================여기까지 table1===============================
    }//search함수의 끝

    //tabl1에서 더블클릭 시 정보를 띄우는 메소드==========================================
    private void lec_search(totalDTO vo) throws IOException {
        //임시 출력 확인
//        System.out.println(vo.getLec_no());

        //이제 받아온 totalDTO객체의 lec_no를 이용해서 나머지 정보들을 출력

        SqlSession ss = factory.openSession();
        List<lecVO> lecVOList = ss.selectList("lec.get_lec", vo.getLec_no());

        if (lecVOList != null) {

            for (lecVO vo2 : lecVOList) {
//                System.out.printf("임시 출력: %s, %s, %s, %s, %s\n", vo2.getAd_name(), vo2.getLec_name(), vo2.getLec_sdate(), vo2.getLec_edate(), vo2.getLec_info());
            }
        } else {
            System.out.println("lec list is null");
        }
        ss.close();
    }
    //tabl1에서 더블클릭 시 정보를 띄우는 메소드==========================================


    //table2를 생성하는 메소드=====================================================
    private void search2() throws IOException {

        SqlSession ss = factory.openSession();
        //========================= table2 생성 ==========================
        col = new String[]{"강의코드", "강의명", "강사명", "시험 이름", "응시 여부 및 결과", "시험 보기(버튼)"};

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
                //================응시 여부==================
                //test_idx를 넘겨줘야됨. -> test_idx에 해당되는 stdno들을 뽑아내서 스트링 배열에 저장함.
                List<stdVO> auth_list = ss.selectList("std.get_test_auth", vo.getTest_idx());

                data[i][4] = "응시 전";
                data[i][5] = "시험 응시"; //시험 보기
                if (auth_list != null && !auth_list.isEmpty()) {

                    for (stdVO avo : auth_list) {
                        if (avo.getStdno().equalsIgnoreCase(dummy.getStdno())) {
                            //두 개의 학번이 같으면? -> 응시 완료
                            data[i][4] = "응시 완료(결과 확인)";
                            data[i][5] = "응시 불가";
                            break;
                        }
                    }
                } else {
                    //auth_list 가 null인 경우는 시험이 존재하지 않음
                    //lec_no가 test_t에 없으면 시험이 존재하지 않음

                }
                //================응시 여부==================

                i++;

            }

        } else {
            System.out.println("test_list = null");

        }

        //JTable갱신
        jTable2.setRowHeight(30); // 테이블 셀 크기
        jTable2.setModel(new DefaultTableModel(data, col)); //setModel 해주고
        jTable2.setDefaultEditor(Object.class, null); //테이블 수정을 막아줘야됨.
//        r.close();
        ss.close();

        // 버튼 column 생성 (시험 응시 버튼)
        jTable2.getColumn("시험 보기(버튼)").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton bt3 = new JButton(value.toString());
            if ("시험 응시".equals(value)) {
                return bt3;
            } else {
                // 그냥 JLabel로 텍스트 출력 (버튼 아님)
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true); // 배경색 설정을 위해 꼭 필요
                label.setBackground(Color.LIGHT_GRAY); // 회색 배경
                label.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬도 가능
                return label;
                // 또는 new JLabel("") 하면 아무것도 안 나옴
            }
        });
        // 버튼 column 생성 (점수 확인 버튼)
        jTable2.getColumn("응시 여부 및 결과").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton bt3 = new JButton(value.toString());
            if ("응시 완료(결과 확인)".equals(value)) {
                return bt3;
            } else {
                // 그냥 JLabel로 텍스트 출력 (버튼 아님)
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true); // 배경색 설정을 위해 꼭 필요
                label.setBackground(Color.LIGHT_GRAY); // 회색 배경
                label.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬도 가능
                return label;
            }
        });

    }//search2 method의 끝

    //==================================================================================
    // init() 메소드================================================================
    private void initComponents() {
        //title north panel ==========
        title = new JPanel();
        lb_t = new JLabel();
        JLabel lb_t2 = new JLabel();
        title.add(lb_t2, BorderLayout.CENTER);
        title.add(lb_t, BorderLayout.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        logout_bt = new JButton("로그아웃");
        exit_bt = new JButton("종료");
        logout_bt3 = new JButton("로그아웃");
        exit_bt3 = new JButton("종료");
        logout_bt2 = new JButton("로그아웃");
        exit_bt2 = new JButton("종료");
        //NetBeans ===================
        JPanel jPanel1 = new JPanel();
        JTabbedPane jTabbedPane1 = new JTabbedPane();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        JPanel jPanel4 = new JPanel();
        jLabel2 = new JLabel();
        // Variables declaration - do not modify
        jButton1 = new JButton();
        image_bt = new JButton("사진 변경");
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
        jLabel_birth = new JLabel();
        JPanel jPanel5 = new JPanel();
        JScrollPane jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        JPanel jPanel6 = new JPanel();
        JScrollPane jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        jPanel2.setLayout(new GridLayout(2, 2));

        //title 로고 ==============================================
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/image.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(52, 47, Image.SCALE_SMOOTH);
        lb_t2.setIcon(new ImageIcon(scaledImage));

        jButton1.setText("사진 변경");
        // =================================================
        jButton4 = new JButton();
        jButton4.setText("정보 변경");


        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(72, Short.MAX_VALUE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addContainerGap(50, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addGap(5, 5, 5)
                                .addComponent(jButton4))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(48, 48, 48)))
        );

        JLabel birth_lb = new JLabel("생년월일 :");
        birth_lb.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        jLabel3.setText("mail :");
        jLabel3.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        jLabel4.setText("이름 :");
        jLabel4.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        jLabel5.setText("주소 :");
        jLabel5.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        jLabel6.setText("학번 :");
        jLabel6.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        jLabel7.setText(" ☎  :");
        jLabel7.setFont(new Font("맑은 고딕", Font.BOLD, 14));


        //===============================================학생 카드 영역 패널=====================================================
//        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
//        jPanel7.setLayout(jPanel7Layout);
//        jPanel7Layout.setHorizontalGroup(
//                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                .addContainerGap()
//                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                                .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
//                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
//                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(jLabel11, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
//                                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
//                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(jLabel8, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
//                                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
//                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(jLabel9, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
//                                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
//                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(jLabel10, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)))
//                                .addContainerGap())
//        );
//        jPanel7Layout.setVerticalGroup(
//                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
//                                .addContainerGap(10, Short.MAX_VALUE)
//                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
//                                .addGap(7, 7, 7)
//                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
//                                .addContainerGap())
//        );

        //==
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
                                                .addComponent(birth_lb, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
//                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGap(30)
                                                .addComponent(jLabel_birth, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
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
                                        .addComponent(birth_lb, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_birth, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );


        //==
        //================================================================================================================
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(logout_bt2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exit_bt2)
                                .addContainerGap())
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(logout_bt2)
                                        .addComponent(exit_bt2))
                                .addContainerGap())
        );

        jTabbedPane1.addTab("강의실", jPanel5);


        jScrollPane2.setViewportView(jTable2);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(logout_bt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exit_bt)
                                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(exit_bt)
                                        .addComponent(logout_bt))
                                .addContainerGap())
        );
        //
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


        JPanel jPanel8 = new JPanel();
        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(logout_bt3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 306, Short.MAX_VALUE)
                                .addComponent(exit_bt3)
                                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addContainerGap(205, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(exit_bt3)
                                        .addComponent(logout_bt3))
                                .addContainerGap())
        );

        jPanel2.add(jPanel8);

        getContentPane().add(title, BorderLayout.NORTH);
        getContentPane().add(jPanel1, BorderLayout.CENTER);
        pack();

    }// </editor-fold>

}
// End of variables declaration

