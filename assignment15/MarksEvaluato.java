package assignment.assignment15;

import java.util.Scanner;

public class MarksEvaluato {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== Online Exam Evaluator =====");

        System.out.print("Number of questions: ");
        int n = sc.nextInt();

        char[] correctAnswers = new char[n];
        char[] studentAnswers = new char[n];

        System.out.println("Enter correct answers:");

        for (int i = 0; i < n; i++) {
            correctAnswers[i] = sc.next().charAt(0);
        }

        System.out.println("Enter student answers (- for unattempted):");

        for (int i = 0; i < n; i++) {
            studentAnswers[i] = sc.next().charAt(0);
        }

        int correct = 0;
        int wrong = 0;
        int unattempted = 0;
        double score = 0;

        for (int i = 0; i < n; i++) {

            if (studentAnswers[i] == '-') {
                unattempted++;
            }
            else if (studentAnswers[i] == correctAnswers[i]) {
                correct++;
                score = score + 2;
            }
            else {
                wrong++;
                score = score - 0.5;
            }
        }

        System.out.print("Correct answers : ");
        for (int i = 0; i < n; i++) {
            System.out.print(correctAnswers[i] + " ");
        }

        System.out.println();

        System.out.print("Student answers : ");
        for (int i = 0; i < n; i++) {
            System.out.print(studentAnswers[i] + " ");
        }

        System.out.println();

        System.out.println("Correct: " + correct +
            " Wrong: " + wrong +
            " Unattempted: " + unattempted);

        System.out.printf("Final Score: %.1f / " + (n * 2), score);

        sc.close();
    }
}