import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class BM25
{
    public static void main(String[] args)
    {
        String filename = "doc1.csv";
        List<List<String>> lines = getCSVdata(filename);

        int N, word1=2, word2=3;
        double k = 1.2, b = 0.75;
        double IDF1, IDF2, L, DF1, DF2, BM25W1W2, BM25W1, BM25W2;


        N = lines.size();
        System.out.println("N: " + N);

        L = calculateL(lines,N);
        System.out.println("L: " + L);

        DF1 = calculateDF(lines, N, word1);
        DF2 = calculateDF(lines, N, word2);
        System.out.println("DF1: " + DF1);
        System.out.println("DF2: " + DF2);

        IDF1 = calculateIDF(N,DF1);
        IDF2 = calculateIDF(N,DF2);
        System.out.println("IDF1: " + IDF1);
        System.out.println("IDF2: " + IDF2);
        System.out.println();

        BM25W1 = calculateBM25(lines,N,k,b,L,IDF1,word1);
        BM25W2 = calculateBM25(lines,N,k,b,L,IDF2,word2);
        System.out.println("BM25 Word1: " + BM25W1);
        System.out.println("BM25 Word2: " + BM25W2);

        BM25W1W2 = BM25W1 + BM25W2;
        System.out.println();

        System.out.println("BM25 Word1&2: " + BM25W1W2);
    }

    private static double calculateL(List<List<String>> lines, int N)
    {
        double L = 0;
        for(int i = 0; i < N; i++)
        {
            L += Integer.parseInt(lines.get(i).get(1));

        }
        L = L/N;

        return L;
    }

    private static double calculateDF(List<List<String>> lines, int N, int wordColumn)
    {
        double DF = 0;
        for(int i = 0; i < N; i++)
        {
            if(Integer.parseInt(lines.get(i).get(wordColumn)) > 0)
            {
                DF++;
            }
        }
        return DF;
    }

    private static double calculateIDF(int N, double DF)
    {
        return Math.log(((N-DF)+0.5)/(DF+0.5));
    }

    private static double calculateBM25(List<List<String>> lines, int N, double k, double b, double L, double IDF, int wordColumn)
    {
        double BM25 = 0;

        System.out.println("Word " + (wordColumn-1) + " TF Values:");
        for(int i = 0; i < N; i++)
        {
            double TF = Integer.parseInt(lines.get(i).get(wordColumn));
            System.out.println("Document " + (i+1) + " TF: " + TF);
            double E = (1-b)+(b*((Integer.parseInt(lines.get(i).get(1)))/L));
            BM25 += IDF*((TF*(k+1))/(TF+(k*E)));
        }
        System.out.println();
        return BM25;
    }

    private static List<List<String>> getCSVdata(String filename)
    {
        File file= new File(filename);
        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);

            while(inputStream.hasNext())
            {
                String line = inputStream.next();
                String[] values = line.split(",");
                lines.add(Arrays.asList(values));
            }

            inputStream.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return lines;
    }
}
