package com.ll;

import java.util.Scanner;

// 명언 앱
public class Main {
    public static void main(String[] args) {
        System.out.println("aa");
        Scanner scanner = new Scanner(System.in);
        String command;
        String content;
        String author;
        int id = 0;

        while (true) {
            System.out.print("명령 : ");
            command = scanner.next();
            if(command.equals("종료")){
                break;
            }

            if(command.equals("등록")){
                System.out.print("명언 : ");
                content = scanner.next();
                System.out.print("작가 : ");
                author = scanner.next();
                id++;
                System.out.println(id + "번 명언이 등록되었습니다.");
            }

            // 4단계까지 완료
            // 등록한 내용을 저장하는 방법 필요
        }
    }
}