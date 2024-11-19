package com.ll;

import java.util.Arrays;
import java.util.Scanner;

class Saying{
    String content;
    String author;
    int id;
    static int count = 1;

    Saying(){}

    Saying(String content, String author){
        this.content = content;
        this.author = author;
        this.id = count++;
        System.out.println(this.id + "번 명언이 등록되었습니다.");
    }
}

public class Main {

    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");
        Scanner scanner = new Scanner(System.in);
        String command;
        String content;
        String author;
        Saying[] sayings = {};

        while (true) {
            System.out.print("명령 : ");
            command = scanner.nextLine();

            if (command.equals("종료")) {
                break;

            }else if (command.equals("등록")) {
                System.out.print("명언 : ");
                content = scanner.nextLine();
                System.out.print("작가 : ");
                author = scanner.nextLine();
                Saying saying = new Saying(content, author);
                sayings = Arrays.copyOf(sayings, sayings.length +1);
                sayings[sayings.length-1] = saying;

            }else if (command.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("------------------");

                for(Saying s : sayings){
                    if(s == null){
                        continue;
                    }else {
                        System.out.println(s.id + " / " + s.author + " / " + s.content);
                    }
                }

            }else if (command.equals("삭제")) {
                System.out.println("명령어는 삭제? id = 의 형식으로 입력해주세요.");

            }else if (command.startsWith("삭제? id =")) {
                String[] parts = command.split("=");
                int delNo = Integer.parseInt(parts[1].strip());

                if(sayings[delNo-1] != null){
                    sayings[delNo-1] = null;
                }else{
                    System.out.println(delNo + "번 명언은 존재하지 않습니다.");
                    continue;
                }

                System.out.println(delNo + "번 명언이 삭제되었습니다.");

            }else if (command.equals("수정")) {
                System.out.println("명령어는 수정? id = 의 형식으로 입력해주세요.");

            }else if (command.startsWith("수정? id =")) {
                String[] parts = command.split("=");
                int modNo = Integer.parseInt(parts[1].strip());

                if(sayings[modNo -1] != null){
                    System.out.println("명언(기존) : " + sayings[modNo-1].content);
                    System.out.print("명언 : ");
                    sayings[modNo-1].content = scanner.nextLine();
                    System.out.println("작가(기존) : " + sayings[modNo-1].author);
                    System.out.print("작가 : ");
                    sayings[modNo-1].author = scanner.nextLine();
                }else{
                    System.out.println(modNo + "번 명언은 존재하지 않습니다.");
                    continue;
                }
                System.out.println(modNo + "번 명언이 수정되었습니다.");

            }else{
                System.out.println("잘못된 명령입니다.");
            }
        }
    }
}