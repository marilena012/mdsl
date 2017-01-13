package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application
{
	//maximum density still life
	static boolean[][] table;
	static boolean[][] solved_table;
	static Scene scene;
	static int n;
	static int max;

	static int h = 450, w = 600;

	static Image blank = new Image("blank.png");
	static Image blue = new Image("blue.png");
	static Image red = new Image("red.png");
	static Image gray = new Image("gray.png");
	static Image redS = new Image("redS.png");

	static Button solveChoco = new Button("Solve Choco");
	static Button solveNaiv = new Button("Solve Alg.naiv");
	static Button solutie = new Button("Incarca solutia");
	static Button celuleLibere = new Button("Sterge celule");
	static ArrayList<boolean[][]> solutii = new ArrayList<boolean[][]>();
	static boolean[][] celLibere;
	static boolean selectareCelule = false;
	static boolean redCell = false;
	static boolean solve = true;

	//game of life
	static boolean stareInit; 
	static int nHLife = 25;
	static int nWLife = 40;
	static int nsh = nHLife + 4;
	static int nsw = nWLife + 4;
	static int[][] careu = new int[nHLife][nWLife];
	static int[][] aux = new int[nsh][nsw];
	static int[][] aux1 = new int[nsh][nsw];
	static int[][] anterior = new int[nHLife][nWLife];
	static GridPane tableLife = new GridPane();
	static int s = h/nHLife;
	static Rectangle[][] celule = new Rectangle[nHLife][nWLife];
	static double updates = 0.5;

	static ImagePattern bluePatter = new ImagePattern(blue);
	static ImagePattern blankPatter = new ImagePattern(blank);

	static Button play = new Button("Play");
	static Button stop = new Button("Stop");


	public static void main(String[] args)
	{
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		menu(stage);

		stage.setResizable(true);
		stage.show();
	}

	@SuppressWarnings("static-access")
	static void menu(Stage stage)
	{
		AnchorPane root = new AnchorPane();
		scene = new Scene(root, w, h);

		scene.getStylesheets().add("mdsl.css");

		stage.setTitle("Life");

		root.prefHeight(h);
		root.prefWidth(w);

		Button life = new Button("Play 'Game of Life'");
		life.getStyleClass().add("bigButon");

		life.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					playLife(stage);
				}
			}
				});

		Label dimLabel = new Label("Dimensiunea careului:");
		TextArea dim = new TextArea();

		dim.setPrefWidth(100);
		dim.setPrefHeight(10);		

		Button play = new Button("Play 'Maximum density still life'");
		play.getStyleClass().add("bigButon");

		play.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					if(dim.getText().length() == 0)
					{
						Alert alert = new Alert(AlertType.INFORMATION, 
								"Atentie! Trebuie sa specificati dimenstiunea careului!", ButtonType.OK);
						alert.show();
					}
					else
					{
						n = Integer.parseInt(dim.getText());
						try
						{
							playGame(stage);
						}
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}
				}
			}
				});

		root.setTopAnchor(life, 100.0);
		root.setLeftAnchor(life, 200.0);
		root.getChildren().add(life);

		root.setTopAnchor(dimLabel, 160.0);
		root.setLeftAnchor(dimLabel, 240.0);
		root.getChildren().add(dimLabel);

		root.setTopAnchor(dim, 180.0);
		root.setLeftAnchor(dim, 250.0);
		root.getChildren().add(dim);

		root.setTopAnchor(play, 230.0);
		root.setLeftAnchor(play, 200.0);
		root.getChildren().add(play);

		stage.setScene(scene);
	}

	static void solveChoco()
	{
		solveChoco.setDisable(true);
		solved_table = new boolean[n][n];

		boolean completat = false;
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(celLibere[i][j] == true || table[i][j] == true)
					completat = true;

		if(completat)
		{
			if(solve)
			{
				NewChocoSolver s = new NewChocoSolver(n, celLibere, table);
				s.solve();
				solved_table = s.getTable();
			}
			else
			{
				Alert alert = new Alert(AlertType.INFORMATION, 
						"Atentie! Celule vii asezate necorespunzator!", ButtonType.OK);
				alert.show();

			}
		}
		else
		{
			ChocoSolver s = new ChocoSolver(n);
			s.solve();
			solved_table = s.getTable();
		}
	}

	static void solveNaiv()
	{
		solveNaiv.setDisable(true);
		solved_table = new boolean[n][n];

		boolean completat = false;
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(celLibere[i][j] == true || table[i][j] == true)
					completat = true;

		if(completat)
		{
			if(solve)
			{
				NewSoverNaiv a = new NewSoverNaiv(n, celLibere, table);
				a.algoritm();
				solved_table = a.getTable();
			}
			else
			{
				Alert alert = new Alert(AlertType.INFORMATION, 
						"Atentie! Celule vii asezate necorespunzator!", ButtonType.OK);
				alert.show();

			}
		}
		else
		{
			SolverNaiv a = new SolverNaiv(n);
			a.algoritm();
			solved_table = a.getTable();
		}
	}

	static boolean verifica()
	{
		int nr = 0;
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(table[i][j])
					nr++;

		if(nr != max)
			return false;

		boolean ok1 = false;
		for(int i = 0; i < solutii.size(); i++)
		{
			boolean ok = true;
			for(int j = 0; j < solutii.get(i).length; j++)
				for(int k = 0; k < solutii.get(i).length; k++)
					if(solutii.get(i)[j][k] != table[j][k])
						ok = false;
			if(ok)
				ok1 = true;
		}
		if(ok1)
			return true;

		return false;
	}

	static void verificaCelule()
	{
		redCell = false;
		solve = true;

		int[][] careu = new int[n + 4][n + 4];

		GridPane careu1 = (GridPane) scene.lookup("#careu");
		Scene scene1 = careu1.getScene();

		for(int i = 0; i < n + 4; i ++)
			for(int j = 0; j < n + 4; j++)
			{
				if(i > 1 && j > 1 && i <= n + 1 && j <= n + 1 && table[i-2][j-2])
					careu[i][j] = 1;
				else
					careu[i][j] = 0;
			}

		for(int i = 2; i < n + 2; i++)
			for(int j = 2; j < n + 2; j++)
			{

				int sum = careu[i-1][j-1] + careu[i-1][j] + careu[i-1][j+1] +
						careu[i][j-1] + careu[i][j+1] +
						careu[i+1][j-1] + careu[i+1][j] + careu[i+1][j+1];

				ImageView patrat = (ImageView) scene1.lookup("#dot-" + (i - 2) + "-" + (j - 2));

				if(careu[i][j] == 1)
				{
					if(sum < 2 || 3 < sum)
					{

						patrat.setImage(red);
						redCell = true;
						if(3 < sum)
							solve = false;
					}

					if(2 <= sum && sum <= 3)
						patrat.setImage(blue);
				}

				if(careu[i][j] == 0)
				{
					if(sum == 3)
					{
						patrat.setImage(redS);
						redCell = true;
					}

					else
						patrat.setImage(blank);
				}
			}

		for(int i = 1; i < n + 3; i ++)
		{
			for(int j = 1; j < n + 3; j++)
			{
				if((i == 1 || i == n + 2 || j == 1 || j == n + 2) && careu[i][j] == 0)
				{
					int sum = careu[i-1][j-1] + careu[i-1][j] + careu[i-1][j+1] +
							careu[i][j-1] + careu[i][j+1] +
							careu[i+1][j-1] + careu[i+1][j] + careu[i+1][j+1];

					int k = i - 2, l = j - 2;
					ImageView patrat = (ImageView) scene1.lookup("#dot-" + k + "-" + l);

					if(sum == 3)
					{
						patrat.setImage(redS);
						redCell = true;
						solve = false;
					}
					else
						patrat.setImage(gray);
				}
			}
		}

		for(int i = 2; i < n + 2; i ++)
		{
			for(int j = 2; j < n + 2; j++)
			{
				if(celLibere[i-2][j-2])
				{
					int sum = careu[i-1][j-1] + careu[i-1][j] + careu[i-1][j+1] +
							careu[i][j-1] + careu[i][j+1] +
							careu[i+1][j-1] + careu[i+1][j] + careu[i+1][j+1];

					int k = i - 2, l = j - 2;
					ImageView patrat = (ImageView) scene1.lookup("#dot-" + k + "-" + l);

					if(sum == 3)
					{
						patrat.setImage(redS);
						redCell = true;
						solve = false;
					}
					else
						patrat.setImage(gray);
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	static void playGame(Stage stage) throws IOException
	{
		if(n < 12)
			incarcaSol();

		AnchorPane root = new AnchorPane();
		scene = new Scene(root, w, h - 10);

		scene.getStylesheets().add("mdsl.css");

		table = new boolean[n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				table[i][j] = false;

		celLibere = new boolean[n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				celLibere[i][j] = false;

		h = (h / n) * n;

		root.prefHeight(h);
		root.prefWidth(w);

		GridPane careu = new GridPane();
		careu.setId("careu");
		careu.setPrefSize(h, h);
		careu.setMaxHeight(h);
		careu.setMaxWidth(w);

		careu.setGridLinesVisible(true);

		for(int k = 0; k < n + 2; k++)
		{
			for(int l = 0; l < n + 2; l++)
			{
				final ImageView dot = new ImageView(blank);
				dot.setFitHeight((h)/(n + 2));
				dot.setFitWidth((h )/(n + 2));				

				if(k > 0 && k < n + 1 && l > 0 && l < n + 1)
				{
					dot.setId("dot-" + (k - 1) + "-" + (l - 1));
					dot.setOnMouseClicked(new EventHandler<MouseEvent>()
							{
						@Override
						public void handle(MouseEvent mouseEvent)
						{
							if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
							{
								if(!selectareCelule)
								{
									String[] id = dot.getId().split("-");
									int x = Integer.parseInt(id[1]), y = Integer.parseInt(id[2]);
									if(table[x][y] && !celLibere[x][y])
									{
										dot.setImage(blank);
										table[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = false;
									}
									else
										if(!celLibere[x][y])
										{
											dot.setImage(blue);
											table[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = true;
										}

									if(verifica())
									{
										Alert alert = new Alert(AlertType.INFORMATION, 
												"Felicitari! Ati gasit o solutie pentru n = " + n + "!", ButtonType.OK);
										alert.show();
									}
									verificaCelule();
								}
								else
								{
									String[] id = dot.getId().split("-");
									if(celLibere[Integer.parseInt(id[1])][Integer.parseInt(id[2])])
									{
										dot.setImage(blank);
										celLibere[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = false;
									}
									else
									{
										dot.setImage(gray);
										celLibere[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = true;
										table[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = false;
									}
									verificaCelule();
								}
							}
						}
							});
				}

				else
				{
					dot.setId("dot-" + (k - 1) + "-" + (l - 1));
					dot.setImage(gray);
				}

				careu.add(dot, l, k);
			}
		}

		careu.setVisible(true);

		Label dimLabel = new Label("Dimensiune:");
		TextField dimText = new TextField();
		dimText.setText(n + "");
		dimText.setFocusTraversable(false);

		dimText.getStyleClass().add("text");

		Button back = new Button("Back");
		Button jocNou = new Button("New game");
		jocNou.setFocusTraversable(false);
		back.setFocusTraversable(false);
		solveChoco.setFocusTraversable(false);
		solveNaiv.setFocusTraversable(false);
		solutie.setFocusTraversable(false);

		if(n > 11)
			solutie.setDisable(true);
		else
			solutie.setDisable(false);

		jocNou.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{

					n = Integer.parseInt(dimText.getText());
					try
					{
						playGame(stage);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
				});

		solveChoco.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					long start, end, start1, end1;
					start1 = System.nanoTime();
					start = System.currentTimeMillis();

					solveChoco();
					solveChoco.setDisable(false);

					for(int i = 0; i < n; i++)
						for(int j = 0; j < n; j++)
						{
							table[i][j] = solved_table[i][j];

							GridPane careu1 = (GridPane) scene.lookup("#careu");
							Scene scene1 = careu1.getScene();
							ImageView patrat = (ImageView) scene1.lookup("#dot-" + i + "-" + j);

							if(solved_table[i][j])
								patrat.setImage(blue);
							else
								if(celLibere[i][j])
									patrat.setImage(gray);
								else
									patrat.setImage(blank);
						}

					verificaCelule();

					end1 = System.nanoTime();
					end = System.currentTimeMillis();
					System.out.println((end - start) + " " + (end1 - start1));
				}
			}
				});

		solveChoco.setDisable(false);

		solveNaiv.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					long start, end, start1, end1;
					start1 = System.nanoTime();
					start = System.currentTimeMillis();

					solveNaiv();
					solveNaiv.setDisable(false);

					for(int i = 0; i < n; i++)
						for(int j = 0; j < n; j++)
						{
							table[i][j] = solved_table[i][j];

							GridPane careu1 = (GridPane) scene.lookup("#careu");
							Scene scene1 = careu1.getScene();
							ImageView patrat = (ImageView) scene1.lookup("#dot-" + i + "-" + j);

							if(solved_table[i][j])
								patrat.setImage(blue);
							else
								if(celLibere[i][j])
									patrat.setImage(gray);
								else
									patrat.setImage(blank);
						}

					verificaCelule();

					end1 = System.nanoTime();
					end = System.currentTimeMillis();
					System.out.println((end - start) + " " + (end1 - start1));
				}
			}
				});

		solutie.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					Random r = new Random();
					int nrSol = r.nextInt(solutii.size());

					for(int i = 0; i < n; i++)
						for(int j = 0; j < n; j++)
						{
							table[i][j] = solutii.get(nrSol)[i][j];

							GridPane careu1 = (GridPane) scene.lookup("#careu");
							Scene scene1 = careu1.getScene();
							ImageView patrat = (ImageView) scene1.lookup("#dot-" + i + "-" + j);

							if(table[i][j])
								patrat.setImage(blue);
							else
								patrat.setImage(blank);
						}

				}
			}
				});

		celuleLibere.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent event)
			{
				//				System.out.println(selectareCelule);
				if(!selectareCelule)
				{
					//					System.out.println(1);
					celuleLibere.setText("Gata");
					selectareCelule = true;
				}

				else
				{
					//					System.out.println(2);
					celuleLibere.setText("Sterge celule");
					selectareCelule = false;
				}
			}

				});

		back.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
			@Override
			public void handle(MouseEvent event)
			{
				menu(stage);
			}
				});

		jocNou.getStyleClass().add("buton");
		solveChoco.getStyleClass().add("buton");
		solveNaiv.getStyleClass().add("buton");
		solutie.getStyleClass().add("buton");
		celuleLibere.getStyleClass().add("buton");
		back.getStyleClass().add("buton");

		root.setTopAnchor(careu, 0.0);
		root.setLeftAnchor(careu, 0.0);
		root.setBottomAnchor(careu, 0.0);
		root.setRightAnchor(careu, 150.0);
		root.getChildren().add(careu);

		root.setTopAnchor(dimLabel, 15.0);
		root.setRightAnchor(dimLabel, 40.0);
		root.getChildren().add(dimLabel);

		root.setTopAnchor(dimText, 10.0);
		root.setRightAnchor(dimText, 10.0);
		root.getChildren().add(dimText);

		root.setTopAnchor(jocNou, 40.0);
		root.setRightAnchor(jocNou, 10.0);
		root.getChildren().add(jocNou);

		root.setTopAnchor(solveChoco, 100.0);
		root.setRightAnchor(solveChoco, 10.0);
		root.getChildren().add(solveChoco);

		root.setTopAnchor(solveNaiv, 160.0);
		root.setRightAnchor(solveNaiv, 10.0);
		root.getChildren().add(solveNaiv);

		root.setTopAnchor(solutie, 220.0);
		root.setRightAnchor(solutie, 10.0);
		root.getChildren().add(solutie);

		root.setTopAnchor(celuleLibere, 280.0);
		root.setRightAnchor(celuleLibere, 10.0);
		root.getChildren().add(celuleLibere);

		root.setTopAnchor(back, 340.0);
		root.setRightAnchor(back, 10.0);
		root.getChildren().add(back);

		stage.setScene(scene);
		stage.setTitle("Maximum density still life");
	}

	@SuppressWarnings("static-access")
	static void playLife(Stage stage)
	{
		AnchorPane root = new AnchorPane();
		scene = new Scene(root, 950, h);
		scene.getStylesheets().add("mdsl.css");

		for(int i = 0; i < nHLife; i++)
			for(int j = 0; j < nWLife; j++)
			{
				careu[i][j] = 0;
				anterior[i][j] = 0;
			}

		Button back = new Button("Back");
		Button load = new Button("Load");
		Button save = new Button("Save");
		Button speedUp = new Button("Speed Up");
		Button slowDown = new Button("Slow Down");
		Button sterge = new Button("Clear");

		for(int i = 0; i < nHLife; i++)
		{
			for(int j = 0; j < nWLife; j++)
			{
				Rectangle r = new Rectangle(i * s, j * s, s, s);

				r.setFill(Color.TRANSPARENT);
				r.setStroke(Color.GREY);

				r.setId("rect-" + i + "-" + j);

				celule[i][j] = r;
				celule[i][j].setFill(blankPatter);

				r.setOnMouseClicked(new EventHandler<MouseEvent>()
						{
					@Override
					public void handle(MouseEvent mouseEvent)
					{
						if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
						{
							String[] id = r.getId().split("-");
							if(careu[Integer.parseInt(id[1])][Integer.parseInt(id[2])] == 1)
							{
								r.setFill(blankPatter);
								careu[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = 0;
								anterior[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = 0;
							}
							else
							{
								r.setFill(bluePatter);
								careu[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = 1;
								anterior[Integer.parseInt(id[1])][Integer.parseInt(id[2])] = 1;
							}
						}
					}
						});

				root.setTopAnchor(r, (double) (i * s));
				root.setLeftAnchor(r, (double) (j * s));
				root.getChildren().add(r);
			}
		}

		KeyFrame speed = new KeyFrame(Duration.millis(1000 * updates), e -> coloreaza());
		Timeline animation = new Timeline(speed);
		animation.setCycleCount(Timeline.INDEFINITE);

		play.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent event)
			{
				stareInit = true;
				animation.play();
			}
				});

		stop.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent event)
			{
				animation.stop();
			}
				});

		back.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent event)
			{
				animation.stop();
				menu(stage);
			}
				});

		load.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent event)
			{
				FileChooser fileChooser = new FileChooser();

				File d = new File(getClass().getResource(".").getFile());
				fileChooser.setInitialDirectory(d);

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);

				fileChooser.setTitle("Incarcare configuratie");
				File file = fileChooser.showOpenDialog(stage);
				if (file != null)
				{
					try
					{
						incarcaConfiguratie(file);
					} 
					catch (NumberFormatException | IOException e)
					{
						e.printStackTrace();
					}
				}
			}
				});

		save.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent event)
			{
				FileChooser fileChooser = new FileChooser();

				File d = new File(getClass().getResource(".").getFile());
				fileChooser.setInitialDirectory(d);

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);

				fileChooser.setTitle("Salvare configuratie");
				File file = fileChooser.showSaveDialog(stage);

				if(file != null)
				{
					try
					{
						salveazaConfiguratie(file);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
				});

		speedUp.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent arg0)
			{
				animation.setRate(animation.getRate() + 0.5);
			}

				});

		slowDown.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent arg0)
			{
				animation.setRate(animation.getRate() - 0.5);
			}

				});

		sterge.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

			@Override
			public void handle(MouseEvent event)
			{
				for(int i = 0; i < nHLife; i++)
					for(int j = 0; j < nWLife; j++)
					{
						careu[i][j] = 0;
						anterior[i][j] = 0;
						celule[i][j].setFill(blankPatter);
					}
			}
				});

		save.getStyleClass().add("buton");
		load.getStyleClass().add("buton");
		play.getStyleClass().add("buton");
		stop.getStyleClass().add("buton");
		back.getStyleClass().add("buton");
		speedUp.getStyleClass().add("buton");
		slowDown.getStyleClass().add("buton");
		sterge.getStyleClass().add("buton");

		root.setRightAnchor(sterge, 120.0);
		root.setTopAnchor(sterge, 50.0);
		root.getChildren().add(sterge);

		root.setRightAnchor(play, 10.0);
		root.setTopAnchor(play, 50.0);
		root.getChildren().add(play);

		root.setRightAnchor(load, 120.0);
		root.setTopAnchor(load, 150.0);
		root.getChildren().add(load);

		root.setRightAnchor(speedUp, 10.0);
		root.setTopAnchor(speedUp, 150.0);
		root.getChildren().add(speedUp);

		root.setRightAnchor(save, 120.0);
		root.setTopAnchor(save, 250.0);
		root.getChildren().add(save);

		root.setRightAnchor(slowDown, 10.0);
		root.setTopAnchor(slowDown, 250.0);
		root.getChildren().add(slowDown);

		root.setRightAnchor(back, 120.0);
		root.setTopAnchor(back, 350.0);
		root.getChildren().add(back);

		root.setRightAnchor(stop, 10.0);
		root.setTopAnchor(stop, 350.0);
		root.getChildren().add(stop);

		stage.setTitle("Conway's Game Of Life");
		stage.setScene(scene);
	}

	static public void transformare()
	{
		for(int i = 0; i < nsh; i++)
			for(int j = 0; j < nsw; j++)
				aux[i][j] = 0;

		for(int i = 0; i < nsh; i++)
			for(int j = 0; j < nsw; j++)
			{
				if(stareInit)
					aux1[i][j] = 0;
			}

		if(stareInit)
			stareInit = false;

		for(int i = 0; i < nHLife; i++)
			for(int j = 0; j < nWLife; j++)
				aux1[i + 2][j + 2] = careu[i][j];

		for(int i = 1; i < nsh - 1; i++)
			for(int j = 1; j < nsw - 1; j++)		
			{
				int sum = aux1[i-1][j-1] + aux1[i-1][j] + aux1[i-1][j+1] + aux1[i][j-1] + aux1[i][j+1] + aux1[i+1][j-1] + aux1[i+1][j] + aux1[i+1][j+1];
				if(aux1[i][j] == 1 &&(sum == 2 || sum == 3))
					aux[i][j] = 1;
				if(aux1[i][j] == 0 && sum == 3)
					aux[i][j] = 1;

				if(i == 1 || i == nsh - 2 || j == 1 || j == nsw - 2)
					aux1[i][j] = aux[i][j];
			}

		for(int i = 0; i < nHLife; i++)
			for(int j = 0; j < nWLife; j++)
				careu[i][j] = aux[i + 2][j + 2];
	}

	static private void coloreaza()
	{		
		for(int i = 0; i < nHLife; i++)
		{
			for(int j = 0; j < nWLife; j++)
			{
				if (careu[i][j] == 1)
					celule[i][j].setFill(bluePatter);
				else
					celule[i][j].setFill(blankPatter);
				anterior[i][j] = careu[i][j];
			}
		}
		transformare();
	}

	static private void incarcaSol() throws NumberFormatException, IOException
	{
		solutii.clear();

		File f = new File("Sol_" + n + ".txt");
		BufferedReader br = new BufferedReader(new FileReader(f));

		int sol = Integer.parseInt(br.readLine());
		br.readLine();
		max = Integer.parseInt(br.readLine());
		br.readLine();

		for(int i = 0; i < sol; i++)
		{			
			boolean[][] solutie = new boolean[n][n];

			for(int j = 0; j < n; j++)
			{
				String[] line = br.readLine().split(" "); 

				for(int k = 0; k < n; k++)
				{
					if(Integer.parseInt(line[k]) == 0)
						solutie[j][k] = false;
					else
						solutie[j][k] = true;
				}
			}
			solutii.add(solutie);
			br.readLine();
		}
		br.close();
	}

	static private void salveazaConfiguratie(File file) throws IOException
	{
		FileWriter fileWriter = null;

		fileWriter = new FileWriter(file);

		for(int i = 0; i < nHLife; i++)
		{
			for(int j = 0; j < nWLife; j++)
				fileWriter.write(anterior[i][j] + " ");
			fileWriter.write("\n");
		}        
		fileWriter.close();
	}

	static private void incarcaConfiguratie(File file) throws NumberFormatException, IOException
	{
		for(int i = 0; i < nHLife; i++)
			for(int k = 0; k < nWLife; k++)
				careu[i][k] = 0;

		BufferedReader br = new BufferedReader(new FileReader(file));

		for(int i = 0; i < nHLife; i++)
		{
			String[] line = br.readLine().split(" "); 

			for(int k = 0; k < nWLife; k++)
				careu[i][k] = Integer.parseInt(line[k]);
		}
		br.close();

		for(int i = 0; i < nHLife; i++)
		{
			for(int j = 0; j < nWLife; j++)
			{
				if (careu[i][j] == 1)
					celule[i][j].setFill(bluePatter);
				else
					celule[i][j].setFill(blankPatter);
			}
		}
	}
}