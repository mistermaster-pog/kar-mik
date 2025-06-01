import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;

public class StarodhomTranslator extends JFrame {
    private final JTextArea inputArea;
    private final JTextArea outputArea;
    private final JButton translateToStarodhomButton;
    private final JButton translateToPolishButton;

    private final HashMap<String, String> polishToStarodhom;
    private final HashMap<String, String> starodhomToPolish;

    public StarodhomTranslator() {
        super("Tłumacz Starodhóm <-> Polski");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);

        translateToStarodhomButton = new JButton("Polski -> Starodhóm");
        translateToPolishButton = new JButton("Starodhóm -> Polski");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(translateToStarodhomButton);
        buttonPanel.add(translateToPolishButton);

        add(new JScrollPane(inputArea), BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        polishToStarodhom = new HashMap<>();
        starodhomToPolish = new HashMap<>();

        loadDictionary("dictionary.csv");
        setupListeners();
    }

    private void loadDictionary(String fileName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                String[] parts = line.strip().split(",");
                if (parts.length >= 2) {
                    String polish = parts[0].trim().toLowerCase();
                    String starodhom = parts[1].trim().toLowerCase();
                    polishToStarodhom.put(polish, starodhom);
                    starodhomToPolish.put(starodhom, polish);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Nie udało się wczytać słownika: " + e.getMessage());
        }
    }

    private void setupListeners() {
        translateToStarodhomButton.addActionListener(e -> translate(polishToStarodhom));
        translateToPolishButton.addActionListener(e -> translate(starodhomToPolish));
    }

    private void translate(HashMap<String, String> dictionary) {
        String[] words = inputArea.getText().trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(dictionary.getOrDefault(word, "[?]")).append(" ");
        }
        outputArea.setText(result.toString().trim());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StarodhomTranslator().setVisible(true));
    }
}
