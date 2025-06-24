package Student.Client;

import Student.vo.stdVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Reader;

public class ModifyDialog extends JFrame {
    //회원 정보를 수정하는 Dialog
    stdVO vo;
    StudentFrame parent;
    private JPanel north_p, p1, p2, p3, p4, p5, p6;
    private JLabel name_lb, stdno_lb, mail_lb, phone_lb, add_lb, title_lb;
    private JTextField name_tf, stdno_tf, mail_tf, phone_tf, add_tf;
    private JButton bt1, bt2;

    public ModifyDialog(stdVO vo, StudentFrame parent){
        this.vo = vo;
        this.parent = parent;
        
        if(parent != null){
            System.out.println("parent 연결 완료");
        }
        //창 설정
        initComponents();
        this.setBounds(200,200,300,500);
        this.setVisible(true);
        setField();

        //리스너========================================================
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //X버튼
                dispose();
            }
        });
        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //저장 버튼
                //stdvo를 이용해서 std table을 UPDATE 해주어야됨.
                try {
                    update();
                    JOptionPane.showMessageDialog(ModifyDialog.this,"저장 완료!");
                    parent.update_card(vo);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //취소 버튼
                dispose();
            }
        });

    }//생성자의 끝
    //UPDATE 함수
    private void update() throws IOException {
        Reader r = Resources.getResourceAsReader("Student/config/conf.xml");

        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(r);
        SqlSession ss = factory.openSession();
        vo.setStd_email(mail_tf.getText());
        ss.update("std.update_mail", vo);
        ss.commit();

        vo.setStd_phone(phone_tf.getText());
        ss.update("std.update_phone",vo);
        ss.commit();

        vo.setStd_address(add_tf.getText());
        ss.update("std.update_add",vo);
        ss.commit();

        if(vo != null) {
            System.out.printf("임시 출력 : %s, %s, %s, %s\n", vo.getStd_name(), vo.getStd_email(), vo.getStd_phone(), vo.getStd_address());
        }else{
            System.out.println("stdvo is null");
        }

        ss.close();
    }
    


    //화면 구성======================================================================
    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(7, 1));

        north_p = new JPanel();
        title_lb = new JLabel("회원 정보 수정");
        title_lb.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        north_p.add(title_lb);
        north_p.setPreferredSize(new Dimension(0,30));
        getContentPane().add(north_p);

        p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        p6 = new JPanel();
        //이름, 학번 (고정)
        name_lb = new JLabel();
        name_lb.setText("이름:  ");
        name_lb.setFont(new Font("맑은 고딕",Font.BOLD,12));
        name_lb.setPreferredSize(new Dimension(60, 55));
        name_tf = new JTextField("이름");
        name_tf.setEditable(false);
        name_tf.setColumns(18);
        p1.add(name_lb);
        p1.add(name_tf);
        getContentPane().add(p1);

        stdno_lb = new JLabel();
        stdno_lb.setText("학번:  ");
        stdno_lb.setFont(new Font("맑은 고딕",Font.BOLD,12));
        stdno_lb.setPreferredSize(new Dimension(60, 55));
        stdno_tf = new JTextField("학번");
        stdno_tf.setEditable(false);
        stdno_tf.setColumns(18);
        p2.add(stdno_lb);
        p2.add(stdno_tf);
        getContentPane().add(p2);


        mail_lb = new JLabel();
        mail_lb.setText("e-mail:  ");
        mail_lb.setFont(new Font("맑은 고딕",Font.BOLD,12));
        mail_lb.setPreferredSize(new Dimension(60, 55));
        mail_tf = new JTextField("mail");
        mail_tf.setColumns(18);
        p3.add(mail_lb);
        p3.add(mail_tf);
        getContentPane().add(p3);

        phone_lb = new JLabel();
        phone_lb.setText("전화번호:  ");
        phone_lb.setFont(new Font("맑은 고딕",Font.BOLD,12));
        phone_lb.setPreferredSize(new Dimension(60, 55));
        phone_tf = new JTextField("번호");
        phone_tf.setColumns(18);
        p4.add(phone_lb);
        p4.add(phone_tf);
        getContentPane().add(p4);

        add_lb = new JLabel();
        add_lb.setText("주소:  ");
        add_lb.setFont(new Font("맑은 고딕",Font.BOLD,12));
        add_lb.setPreferredSize(new Dimension(60, 55));
        add_tf = new JTextField("주소");
        add_tf.setColumns(18);
        p5.add(add_lb);
        p5.add(add_tf);
        getContentPane().add(p5);

        bt1 = new JButton("저장");
        bt1.setFont(new Font("맑은 고딕",Font.BOLD,15));
        bt2 = new JButton("취소");
        bt2.setFont(new Font("맑은 고딕",Font.BOLD,15));
        p6.add(bt1);
        p6.add(bt2);
        getContentPane().add(p6);

        JLabel dummy = new JLabel(); // 빈 라벨
        dummy.setFocusable(true); // 포커스 받을 수 있게
        p6.add(dummy); // 어디든 추가
        SwingUtilities.invokeLater(() -> {
            dummy.requestFocusInWindow(); // 강제로 포커스
        });
        pack();

    }
    //화면 구성======================================================================

    //TextField 셋팅================================================================
    private void setField(){
        //각각의 tf들을 setting 해주는 메서드
        // 순서대로 이름, 학번, mail, 번호, 주소
        name_tf.setText(vo.getStd_name());
        name_tf.setBackground(Color.LIGHT_GRAY);
        name_tf.setFocusable(false); //tab으로도 이동 불가
        name_tf.setCaretColor(new Color(0, 0, 0, 0)); // 커서 제거
        name_tf.setDisabledTextColor(Color.BLACK); // 글자색 유지

        stdno_tf.setText("2025 -" + vo.getStdno());
        stdno_tf.setBackground(Color.LIGHT_GRAY);
        stdno_tf.setFocusable(false); //tab으로도 이동 불가
        stdno_tf.setCaretColor(new Color(0, 0, 0, 0)); // 커서 제거
        stdno_tf.setDisabledTextColor(Color.BLACK); // 글자색 유지

        mail_tf.setText(vo.getStd_email());
        mail_tf.setCaretColor(new Color(0, 0, 0, 0)); // 초기엔 커서 안 보이게

        phone_tf.setText(vo.getStd_phone());
        phone_tf.setCaretColor(new Color(0, 0, 0, 0)); // 초기엔 커서 안 보이게

        add_tf.setText(vo.getStd_address());
        add_tf.setCaretColor(new Color(0, 0, 0, 0)); // 초기엔 커서 안 보이게

        // 포커스 이벤트로 커서 색 조절
        mail_tf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                mail_tf.setCaretColor(Color.BLACK); // 포커스 얻으면 커서 보이게
            }

            @Override
            public void focusLost(FocusEvent e) {
                mail_tf.setCaretColor(new Color(0, 0, 0, 0)); // 포커스 잃으면 다시 안 보이게
            }
        });

        phone_tf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                phone_tf.setCaretColor(Color.BLACK); // 포커스 얻으면 커서 보이게
            }

            @Override
            public void focusLost(FocusEvent e) {
                phone_tf.setCaretColor(new Color(0, 0, 0, 0)); // 포커스 잃으면 다시 안 보이게
            }
        });

        add_tf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                add_tf.setCaretColor(Color.BLACK); // 포커스 얻으면 커서 보이게
            }

            @Override
            public void focusLost(FocusEvent e) {
                add_tf.setCaretColor(new Color(0, 0, 0, 0)); // 포커스 잃으면 다시 안 보이게
            }
        });
    }
    //TextField 셋팅================================================================

}
