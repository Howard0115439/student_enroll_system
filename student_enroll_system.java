//reference:https://www.youtube.com/watch?v=5vzCjvUwMXg

import java.util.*;
import java.sql.*;

public class Main
{
    static Statement st;
    static Connection con;

    public static void main(String[] args) throws Exception
    {
        while(true)
        {
            initial();
            if(table_exist("student")==false && table_exist("course")==false && table_exist("link")==false) create_new_table();

            System.out.println("//-----------------------------------------------------");
            System.out.println("1.student enroll program");
            System.out.println("2.add new course");
            System.out.println("3.student enroll the course");
            System.out.println("4.which students are in each course");
            System.out.println("5.which courses each student is in");
            System.out.println("6.which courses and what times each course is for a given student on a given day of the week");
            System.out.println("7.print student table & course table");

            System.out.println("please press 1~7");
            Scanner scan = new Scanner(System.in);
            String str = scan.nextLine();

            switch(str)
            {
                case "1":
                {
                    System.out.println("please enter student_name");
                    String name = scan.nextLine();
                    student_enroll_program(name);

                    break;
                }
                case "2":
                {
                    System.out.println("please enter course_name");
                    String course_name = scan.nextLine();
                    System.out.println("please enter course_day : Sunday~Saturday");
                    String course_day = scan.nextLine();
                    System.out.println("please enter course_time : 00:00~24:00");
                    String course_time = scan.nextLine();
                    System.out.println("please enter course_teacher");
                    String course_teacher = scan.nextLine();
                    add_new_course(course_name,course_day,course_time,course_teacher);

                    break;
                }
                case "3":
                {
                    print_table();
                    System.out.println("please enter student_id");
                    String student_id = scan.nextLine();
                    System.out.println("please enter course_id");
                    String course_id = scan.nextLine();
                    student_enroll_the_course(student_id,course_id);

                    break;
                }
                case "4":
                {
                    print_table();
                    System.out.println("please enter course_id");
                    String course_id = scan.nextLine();
                    which_students_are_in_each_course(course_id);

                    break;
                }
                case "5":
                {
                    print_table();
                    System.out.println("please enter student_name");
                    String student_name = scan.nextLine();
                    System.out.println("please enter student_id");
                    String student_id = scan.nextLine();
                    which_courses_each_student_is_in(student_name,student_id);

                    break;
                }
                case "6":
                {
                    System.out.println("please enter student_name");
                    String student_name = scan.nextLine();
                    System.out.println("please enter student_id");
                    String student_id = scan.nextLine();
                    System.out.println("please enter course_day");
                    String course_day = scan.nextLine();
                    last_function(student_name,student_id,course_day);

                    break;
                }
                case "7":
                {
                    print_table();

                    break;
                }
                default:
                {
                    System.out.println("you press wrong number");
                    break;
                }
            }
            end();
        }
    }

    public static void last_function(String student_name,String student_id,String course_day) throws Exception
    {
        String str="SELECT c.course_id,c.course_name,c.course_time FROM student s JOIN link l ON s.student_id=l.student_id JOIN course c ON l.course_id=c.course_id WHERE s.student_name='"+student_name+"' and s.student_id='"+student_id+"' AND c.course_day='"+course_day+"';";

        ResultSet rs = st.executeQuery(str);
        ResultSetMetaData rsMetaData = rs.getMetaData();

        while(rs.next()==true)
        {
            String s=rs.getString(rsMetaData.getColumnName(1))+" "+rs.getString(rsMetaData.getColumnName(2))+" "+rs.getString(rsMetaData.getColumnName(3));

            System.out.println(s);
        }

        if(student_exist(student_name)==false) System.out.println("Wrong student name");
        if(student_id_exist(student_id)==false) System.out.println("Wrong student id");
        if(course_day_exist(course_day)==false) System.out.println("Wrong course day");
    }

    public static void which_courses_each_student_is_in(String student_name,String student_id) throws Exception
    {
        String str="SELECT c.course_id,c.course_name FROM course c JOIN link l ON l.course_id=c.course_id JOIN student s ON s.student_id=l.student_id WHERE s.student_name='"+student_name+"' and s.student_id="+student_id+";";

        if(student_exist(student_name)==true && student_id_exist(student_id)==true)
        {
            ResultSet rs = st.executeQuery(str);
            ResultSetMetaData rsMetaData = rs.getMetaData();

            while(rs.next()==true)
            {
                String s=rs.getString(rsMetaData.getColumnName(1))+" "+rs.getString(rsMetaData.getColumnName(2));

                System.out.println(s);
            }
        }

        if(student_exist(student_name)==false) System.out.println("Wrong student name");
        if(student_id_exist(student_id)==false) System.out.println("Wrong student id");
    }
    public static void which_students_are_in_each_course(String course_id) throws Exception
    {
        String str="SELECT s.student_id,s.student_name FROM student s JOIN link l ON s.student_id=l.student_id JOIN course c ON l.course_id=c.course_id where c.course_id='"+course_id+"'";

        ResultSet rs = st.executeQuery(str);
        ResultSetMetaData rsMetaData = rs.getMetaData();

        while(rs.next()==true)
        {
            String s=rs.getString(rsMetaData.getColumnName(1))+" "+rs.getString(rsMetaData.getColumnName(2));

            System.out.println(s);
        }

        if(course_id_exist(course_id)==false)                System.out.println("Wrong course_id");
    }

    public static void student_enroll_the_course(String studentID,String courseID) throws Exception
    {
        List<String> student_id=new ArrayList<>();
        List<String> course_id=new ArrayList<>();
        String get_student_id="select student_id from student where student_id='"+studentID+"'";
        String get_course_id="select course_id from course where course_id='"+courseID+"'";

        ResultSet rs = st.executeQuery(get_student_id);
        ResultSetMetaData rsMetaData = rs.getMetaData();

        while(rs.next()==true)
        {
            student_id.add(rs.getString(rsMetaData.getColumnName(1)+""));
        }

        ResultSet rs1 = st.executeQuery(get_course_id);
        ResultSetMetaData rsMetaData1 = rs1.getMetaData();

        while(rs1.next()==true)
        {
            course_id.add(rs1.getString(rsMetaData1.getColumnName(1)+""));
        }

        if(student_id_exist(studentID)==false)      System.out.println("Wrong student_id");
        if(course_id_exist(courseID)==false)      System.out.println("Wrong course_id");

        for(int i=0;i<course_id.size();i++)
        {
            for(int j=0;j<student_id.size();j++)
            {
                String add_course="insert into link values("+student_id.get(j)+","+course_id.get(i)+");";
                if(link_data_exist(student_id.get(j),course_id.get(i))==false)    st.executeUpdate(add_course);
                else System.out.println("student has enrolled");
            }
        }
        print_link_table();
    }

    public static boolean link_data_exist(String student_id,String course_id) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String link_data_str="select * from link where student_id='"+student_id+"' and course_id='"+course_id+"'";
        ResultSet rs=st.executeQuery(link_data_str);

        if(rs.next()==true)     flag=true;

        return flag;
    }

    public static boolean course_day_exist(String course_day) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String course_day_str="select course_day from course where course_day='"+course_day+"'";
        ResultSet rs=st.executeQuery(course_day_str);

        if(rs.next()==true)     flag=true;

        return flag;
    }

    public static boolean course_id_exist(String course_id) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String str="select course_id from course where course_id='"+course_id+"'";
        ResultSet rs=st.executeQuery(str);

        if(rs.next()==true)     flag=true;

        return flag;
    }
    public static boolean student_id_exist(String student_id) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String student_name_str="select student_id from student where student_id='"+student_id+"'";
        ResultSet rs=st.executeQuery(student_name_str);

        if(rs.next()==true)     flag=true;

        return flag;
    }
    public static boolean student_exist(String student_name) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String student_name_str="select student_name from student where student_name='"+student_name+"'";
        ResultSet rs=st.executeQuery(student_name_str);

        if(rs.next()==true)     flag=true;

        return flag;
    }

    public static boolean course_exist(String course_name) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String course_name_str="select course_name from course where course_name='"+course_name+"'";
        ResultSet rs=st.executeQuery(course_name_str);

        if(rs.next()==true)     flag=true;

        return flag;
    }

    public static boolean course_teacher_exist(String course_teacher) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String course_teacher_str="select course_teacher from course where course_teacher='"+course_teacher+"'";
        ResultSet rs=st.executeQuery(course_teacher_str);

        if(rs.next()==true)     flag=true;

        return flag;
    }

    public static boolean course_time_exist(String course_time) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String course_time_str="select course_time from course where course_time='"+course_time+"'";
        ResultSet rs=st.executeQuery(course_time_str);

        if(rs.next()==true)     flag=true;

        return flag;
    }

    public static boolean course_exist(String course_name,String course_day,String course_time,String course_teacher) throws Exception				//false=people not exist, true=people exist
    {
        boolean flag=false;

        String course_name_str="select course_name from course where course_name='"+course_name+"' and course_day='"+course_day+"' and course_time='"+course_time+"' and course_teacher='"+course_teacher+"'";
        ResultSet rs=st.executeQuery(course_name_str);

        if(rs.next()==true)     flag=true;

        return flag;
    }

    public static void add_new_course(String course_name,String course_day,String course_time,String course_teacher) throws Exception
    {
        String insert_people="insert into course values(default,'"+course_name+"','"+course_day+"','"+course_time+"','"+course_teacher+"');";

        if(course_exist(course_name,course_day,course_time,course_teacher)==false)      st.executeUpdate(insert_people);
        else                                                                            System.out.println("course name has existed");

        print_table();
    }
    public static void student_enroll_program(String name) throws Exception
    {
        String insert_people="insert into student values(default,'"+name+"');";

        st.executeUpdate(insert_people);
        print_table();
    }

    public static boolean table_exist(String tableName) throws Exception			//false=table not exist, true=table exist
    {
        boolean flag = false;

        Connection conn = st.getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        String type [] = {"TABLE"};
        ResultSet rs = meta.getTables(null, null, tableName, type);
        flag = rs.next();

        return flag;
    }

    public static void create_new_table() throws Exception
    {
        String create_student_table="create table student(student_id INT primary key auto_increment,student_name varchar(50) not null);";
        String create_course_table="create table course(course_id INT primary key auto_increment,course_name varchar(50) not null,course_day varchar(50) not null,course_time varchar(50) not null,course_teacher varchar(50) not null);";
        String create_link_table="create table link(student_id int not null,course_id int not null,constraint link_to__student foreign key(student_id) references student(student_id),constraint link_to_course foreign key(course_id) references course(course_id));";

        if(table_exist("student")==false && table_exist("course")==false && table_exist("link")==false)
        {
            st.executeUpdate(create_student_table);
            st.executeUpdate(create_course_table);
            st.executeUpdate(create_link_table);
        }
        else                                            System.out.println("table name has existed");
    }

    public static void print_link_table() throws Exception
    {
        String link_table="select * from link";

        System.out.println();
        System.out.println("print_link_table");

        ResultSet rs = st.executeQuery(link_table);
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int column_count=get_column_count("student");
        for(int i=1;i<=column_count;i++)
        {
            System.out.print(rsMetaData.getColumnName(i));
            if(i!=column_count)       System.out.print(" ");
            else                            System.out.println();
        }

        while(rs.next()==true)
        {
            String str="";

            for(int i=1;i<=column_count;i++)
            {
                str+=rs.getString(rsMetaData.getColumnName(i)+"");
                if(i!=column_count)       str+="          ";
            }
            System.out.println(str);
        }
    }

    public static void print_table() throws Exception
    {
        String student_table="select * from student order by student_id";
        String course_table="select * from course order by course_id";

        System.out.println();
        System.out.println("print_student_table");

        ResultSet rs = st.executeQuery(student_table);
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int column_count=get_column_count("student");
        for(int i=1;i<=column_count;i++)
        {
            System.out.print(rsMetaData.getColumnName(i));
            if(i!=column_count)       System.out.print(" ");
            else                            System.out.println();
        }

        while(rs.next()==true)
        {
            String str="";

            for(int i=1;i<=column_count;i++)
            {
                str+=rs.getString(rsMetaData.getColumnName(i)+"");
                if(i!=column_count)       str+="          ";
            }
            System.out.println(str);
        }



        System.out.println();
        System.out.println("print_course_table");

        rs = st.executeQuery(course_table);
        rsMetaData = rs.getMetaData();
        column_count=get_column_count("course");
        for(int i=1;i<=column_count;i++)
        {
            System.out.print(rsMetaData.getColumnName(i));
            if(i!=column_count)       System.out.print(" ");
            else                            System.out.println();
        }

        while(rs.next()==true)
        {
            String str="";

            for(int i=1;i<=column_count;i++)
            {
                str+=rs.getString(rsMetaData.getColumnName(i)+"");
                if(i!=column_count)       str+="          ";
            }
            System.out.println(str);
        }
    }

    public static int get_column_count(String table_name) throws Exception
    {
        PreparedStatement ps=con.prepareStatement("select * from "+table_name);
        ResultSet rs=ps.executeQuery();
        ResultSetMetaData rsmd=rs.getMetaData();

        int columnsNumber = rsmd.getColumnCount();

        return columnsNumber;
    }

    public static void initial() throws Exception
    {
        String url="jdbc:mysql://127.0.0.1:3306/database_hw";
        String uname="root";
        String pass="1234";

        Class.forName("com.mysql.cj.jdbc.Driver");
        con=DriverManager.getConnection(url,uname,pass);
        st=con.createStatement();
    }

    public static void end() throws Exception
    {
        st.close();
        con.close();
    }
}