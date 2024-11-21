package com.ll;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Saying{
    String content;
    String author;
    int id;
    static int lastId;

    Saying(){}

    Saying(String content, String author){
        this.content = content;
        this.author = author;
        this.id = ++lastId;
        System.out.println(this.id + "번 명언이 등록되었습니다.");
    }

    Saying(int id, String content, String author){
        this.content = content;
        this.author = author;
        this.id = id;
    }
}

public class Main {
    static Saying findSaying(int targetNum, ArrayList<Saying> sayings){ // 객체 찾기
        for(Saying saying : sayings){
            if(saying.id == targetNum){
                return saying;
            }
        }
        return null;
    }

    static void saveFile(Saying saying) { // 파일 저장
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n")
                .append("\"ID\" : ").append(saying.id).append(",\n ")
                .append("\"author\" : \"").append(saying.author).append("\",\n")
                .append("\"content\" : \"").append(saying.content).append("\"\n")
                .append("}");

        try(FileWriter writer = new FileWriter("DB/wiseSaying/" + saying.id + ".json")){
            writer.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stringBuilder.setLength(0);
        stringBuilder.append(Saying.lastId);

        try(FileWriter writer = new FileWriter("DB/wiseSaying/lastId.text")){
            writer.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void delFile(int targetNum) { // 파일 삭제
        File file = new File("DB/wiseSaying/" + targetNum + ".json");
        if(file.delete()){
        }else{
            System.out.println("파일을 삭제하지 못했습니다.");
        }
    }

    static ArrayList<Saying> sortList(ArrayList<Saying> sayings) { // 배열 정렬하기
        ArrayList<Saying> newList = new ArrayList<>();
        for(int i = 0 ; i < Saying.lastId ; i++){
            newList.add(new Saying());
        }
        for(Saying saying : sayings){
            newList.set(newList.size() - saying.id, saying);
        }
        while(newList.remove(findSaying(0,newList))){}
        return newList;
    }

    static void readDB(ArrayList<Saying> sayings) { // 지난 파일 읽어오기
        // DB 폴더 확인 및 만들기
        File folder = new File("DB/wiseSaying");
        if(!folder.exists()){
            folder.mkdirs();
        }

        File[] files = new File("DB/wiseSaying").listFiles();
        for(File file : files){
            if(file.toString().equals("DB/wiseSaying/.DS_Store")){
                continue;
            }else if(file.toString().equals("DB/wiseSaying/data.json")){
                continue;
            }else if(file.toString().equals("DB/wiseSaying/lastId.text")){
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                    Saying.lastId = Integer.parseInt(bufferedReader.readLine());
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String fileContent;
                List<String> list = new ArrayList<>();
                while ((fileContent = bufferedReader.readLine()) != null){
                    list.add(fileContent);
                }

                ArrayList<String> outputList = new ArrayList<>();
                for(String s : list){
                    if(s.equals("{") || s.equals("}")){
                        continue;
                    }
                    String[] elementList = s.split(":");
                    outputList.add(elementList[1].replaceAll("[\",]","").strip());
                }

                Saying savingFromBefore = new Saying(Integer.parseInt(outputList.get(0)),outputList.get(1),outputList.get(2)); // 가져온 내용으로 객체 생성
                sayings.add(savingFromBefore);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command;
        String content;
        String author;
        ArrayList<Saying> sayings = new ArrayList<>();

        readDB(sayings); // 기존 내용 불러옴

        System.out.println("== 명언 앱 ==");

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
                sayings.addFirst(saying);

                // 파일 생성
                saveFile(saying);

            }else if (command.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("------------------");

                for(Saying s : sortList(sayings)) {
                    System.out.println(s.id + " / " + s.author + " / " + s.content);
                }

            }else if (command.startsWith("삭제? id =")) {
                String[] parts = command.split("=");
                int delNo = Integer.parseInt(parts[1].strip());

                Saying targetSaying = findSaying(delNo, sayings);
                if(targetSaying == null){
                    System.out.println(delNo + "번 명언은 존재하지 않습니다.");
                }else{
                    sayings.remove(findSaying(delNo, sayings));
                    System.out.println(delNo + "번 명언이 삭제되었습니다.");

                    delFile(delNo); // 파일도 삭제
                }

            }else if (command.startsWith("수정? id =")) {
                String[] parts = command.split("=");
                int modNo = Integer.parseInt(parts[1].strip());

                Saying targetSaying = findSaying(modNo, sayings);
                if(targetSaying == null){
                    System.out.println(modNo + "번 명언은 존재하지 않습니다.");
                }else{
                    System.out.println("명언(기존) : " + targetSaying.content);
                    System.out.print("명언 : ");
                    targetSaying.content = scanner.nextLine();

                    System.out.println("작가(기존) : " + targetSaying.author);
                    System.out.print("작가 : ");
                    targetSaying.author = scanner.nextLine();

                    saveFile(targetSaying);

                    System.out.println(modNo + "번 명언이 수정되었습니다.");
                }
            }else if (command.equals("빌드")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[");

                for(Saying s : sayings){
                    stringBuilder.append("\n{\n")
                            .append("\"id\" : ").append(s.id).append(",\n")
                            .append("\"content\" : \"").append(s.content).append("\",\n")
                            .append("\"author\" : \"").append(s.author).append("\"\n")
                            .append("},");
                }

                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append("\n]");

                try(FileWriter writer = new FileWriter("DB/wiseSaying/data.json")){
                    writer.write(stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("data.json 파일의 내용이 갱신되었습니다.");

            }
            //잘못된 명령 입력 모음
            else if (command.equals("삭제")) {
                System.out.println("명령어는 삭제? id = 의 형식으로 입력해주세요.");

            }else if (command.equals("수정")) {
                System.out.println("명령어는 수정? id = 의 형식으로 입력해주세요.");

            }else{
                System.out.println("잘못된 명령입니다.");
            }
        }
    }
}