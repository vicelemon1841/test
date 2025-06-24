package Student.Client;

import Student.vo.lecVO;
import Student.vo.totalDTO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Reader;

public class LecDialog extends JFrame {
    //강의실 더블클릭 시 켜지는 dialog

    totalDTO vo;
    JPanel total_p, north_p, center_p, p1, p1_, p2, p3, p4, p5;
    lecVO lecvo;
    JLabel title, label1_, label2_, label3_, label4_, label5_;
    JTextArea desc;
    public LecDialog(totalDTO vo){
        this.vo = vo;

        //전체 패널
        total_p = new JPanel(new BorderLayout());
        //NORTH 패널
        north_p = new JPanel(new GridBagLayout());

        title = new JLabel("강의명", SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER); // 수직 가운데
        north_p.add(title);
        north_p.setPreferredSize(new Dimension(0,80));
        total_p.add(north_p,BorderLayout.NORTH);
        total_p.add(createSeparator());
        //중심 패널
        center_p = new JPanel();
        center_p.setLayout(new BoxLayout(center_p, BoxLayout.Y_AXIS));


        p1 = new JPanel(new GridLayout(1,2));
        p1.add(new JLabel("강사",SwingConstants.CENTER));
        p1.add(createSeparator2());
        p1.add(label1_ = new JLabel("강사이름",SwingConstants.LEFT));


        p2 = new JPanel(new GridLayout(1,2));
        p2.add(new JLabel("강의 설명",SwingConstants.CENTER));
        p2.add(createSeparator2());
        p2.add(label2_ = new JLabel("강의 설명",SwingConstants.LEFT));

        p3 = new JPanel(new GridLayout(1,3));
        p3.add(new JLabel("강의 인원",SwingConstants.CENTER));
        p3.add(createSeparator2());
        p3.add(label3_ = new JLabel("limits" + " 명",SwingConstants.LEFT));


        p4 = new JPanel(new GridLayout(1,3));
        p4.add(new JLabel("개강일",SwingConstants.CENTER));
        p4.add(createSeparator2());
        p4.add(label4_ = new JLabel("개강일          ~"),SwingConstants.LEFT);


        p5 = new JPanel(new GridLayout(1,3));
        p5.add(new JLabel("종강일",SwingConstants.CENTER));
        p5.add(createSeparator2());
        p5.add(label5_ = new JLabel("종강일"),SwingConstants.LEFT);

        JPanel[] panels = {p1, p2, p3, p4, p5};
        for (JPanel p : panels) {
            p.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        }

        // 각 행 추가
        center_p.add(p1);
        center_p.add(createSeparator());

        center_p.add(p2);
        center_p.add(createSeparator());

        center_p.add(p3);
        center_p.add(createSeparator());

        center_p.add(p4);
        center_p.add(createSeparator());

        center_p.add(p5);




        center_p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)  // 안쪽 여백
        ));

        total_p.add(center_p);

        this.add(total_p);

        //창 설정
        this.setBounds(300, 200, 800, 600);
        this.setVisible(true);

        //이벤트 감지자
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        try {
            setLabel();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//생성자의 끝
    //가로 구분선 생성 메소드
    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.GRAY);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        return sep;
    }
    //세로 구분선 생성
    private JSeparator createSeparator2(){
        JSeparator vLine = new JSeparator(SwingConstants.VERTICAL);
        vLine.setForeground(Color.GRAY);
        vLine.setPreferredSize(new Dimension(1, center_p.getHeight())); // 폭 1px, 높이 30px
        vLine.setMaximumSize(new Dimension(1, Integer.MAX_VALUE));
        return vLine;
    }
    private JPanel createRow(String leftText, JLabel rightLabel) {
        JPanel row = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        // 왼쪽 라벨 (30%)
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel leftLabel = new JLabel(leftText, SwingConstants.CENTER);
        row.add(leftLabel, gbc);

        // 가운데 세로 구분선
        gbc.gridx = 1;
        gbc.weightx = 0;
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 30));
        row.add(sep, gbc);

        // 오른쪽 라벨 (70%)
        gbc.gridx = 2;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        rightLabel.setHorizontalAlignment(SwingConstants.CENTER);  // 중앙 정렬
        row.add(rightLabel, gbc);

        // ✅ 아래에 가로줄 추가
        JSeparator hSep = new JSeparator(SwingConstants.HORIZONTAL);
        hSep.setForeground(Color.LIGHT_GRAY);
        hSep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // ✅ row + hSep을 감싸는 래퍼 패널 생성
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // 줄 + 선 높이 포함
        wrapper.add(row);
        wrapper.add(hSep);  // 이게 반드시 높이를 가짐

        return wrapper;
    }


    //각 label들을 set해주는 메소드
    private void setLabel() throws IOException {
        Reader r = Resources.getResourceAsReader("Student/config/conf.xml");

        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(r);
        SqlSession ss = factory.openSession();
        lecvo = ss.selectOne("lec.get_lec",vo.getLec_no());
//        if(lecvo != null) {
//            System.out.printf("임시 출력 : %s, %s, %s, %s\n", lecvo.getLec_name(), lecvo.getLec_info(), lecvo.getAd_name(), lecvo.getLec_limit());
//        }else{
//            System.out.println("lecvo is null");
//        }
        title.setText(lecvo.getLec_name());
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        label1_.setText(lecvo.getAd_name());
        label2_.setText(lecvo.getLec_info());
        label3_.setText(lecvo.getLec_limit());
        label4_.setText(lecvo.getLec_sdate()+"  ~");
        label5_.setText(lecvo.getLec_edate());

        ss.close();
    }

}
