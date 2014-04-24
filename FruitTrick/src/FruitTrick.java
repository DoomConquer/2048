import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.util.Random;
import java.awt.*;
import javax.swing.*;

/**
 * @author Black
 * @Email: lzcn0775@126.com
 * @version 1.00 2014/3/16
 */
public class FruitTrick {
    public static void main(String[] args) {
    	GameFrame gameFrame=new GameFrame();
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	gameFrame.setVisible(true);
    	gameFrame.setResizable(false);
    }
}

class GameFrame extends JFrame{
	public GameFrame(){
		this.setSize(400,400);
		this.setTitle("FruitTrick");
		int ScreenWidth=java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int ScreenHeight=java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(ScreenWidth/2-this.getSize().width/2,ScreenHeight/2-this.getSize().height/2);
		GamePanel gamePanel=new GamePanel(4);
		this.add(gamePanel);
		this.addKeyListener(new KeyAction(gamePanel));
	}
}

class GamePanel extends JPanel{
	private JButton[][] btnArray;
	private int[][] numArray;
	private ImageIcon[] imageiconArray;
	private int num;                //number of not blank
	private int n;
	private Random ran=new Random();
	private boolean isMoved=false;
	private boolean isGameOver=false;
	private boolean isWin=false;
	public GamePanel(int n){
		this.setLayout(new GridLayout(n,n));
		btnArray=new JButton[n][n];
		numArray=new int[n][n];
		imageiconArray=new ImageIcon[n*n];
		for(int i=0;i<n*n;i++){
			if(i<9)
				imageiconArray[i]=new ImageIcon("..\\image\\fruit_0"+(i+1)+".png");
			else
				imageiconArray[i]=new ImageIcon("..\\image\\fruit_"+(i+1)+".png");
		}

		this.n=n;
		num=n*n;
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				JButton button=new JButton();
				button.setBackground(Color.pink);
				button.setFocusable(false);
				btnArray[i][j]=button;
				this.add(button);
			}
		}
			newRandom();
			newRandom();
	}

	private void newRandom(){
		int index=ran.nextInt(num);
		num--;
		int k=0;
		for(int i=0;i<n&&k<=index;i++){
			for(int j=0;j<n&&k<=index;j++){
				if(numArray[i][j]==0){
					if(k==index){
						btnArray[i][j].setIcon(imageiconArray[0]);
						numArray[i][j]=2;
					}
					k++;
				}
			}
		}
	}

	public void update(){
		boolean emptyflag=false;
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++){
				if(numArray[i][j]==0){
					btnArray[i][j].setBackground(Color.pink);
					btnArray[i][j].setIcon(null);
					emptyflag=true;
				}
				else if(numArray[i][j]==Math.pow((double)2,(double)n*n-5)){        //not proved
					isWin=true;
				}
				else{
					int imageIndex=(int)(Math.log(Double.parseDouble(numArray[i][j]+""))/Math.log((double)2));
					btnArray[i][j].setIcon(imageiconArray[imageIndex-1]);
				}
			}
			if(!emptyflag){                    //check neighbour if they can merge
				boolean isMerged=false;
				outA:for(int i=0;i<n;i++)
					for(int j=0;j<n-1;j++){
						if(numArray[i][j]==numArray[i][j+1]){
							isMerged=true;
							break outA;
						}
					}
					outB:for(int j=0;j<n;j++)
					for(int i=0;i<n-1;i++){
						if(numArray[i][j]==numArray[i+1][j]){
							isMerged=true;
							break outB;
						}
					}
					if(!isMerged)
						isGameOver=true;
			}
			if(isMoved){
				newRandom();
				isMoved=false;
			}
	}
	private void move(int i,int j,int m,int n){
			numArray[i][j]=numArray[m][n];
			numArray[m][n]=0;
			isMoved=true;
	}
	private void merge(int i,int j,int m,int n){
			numArray[i][j]<<=1;
			numArray[m][n]=0;
			num++;
			isMoved=true;
	}
	public void moveUp(){
		for(int j=0;j<n;j++){
			for(int i=0;i<n;i++){              //merge into one
				int k=0;
				if(numArray[i][j]!=0){
					for(k=i+1;k<n;k++){
						if(numArray[k][j]!=0&&numArray[i][j]==numArray[k][j]){
							merge(i,j,k,j);
						}
						else if(numArray[k][j]!=0&&numArray[k][j]!=0&&numArray[i][j]!=numArray[k][j]){
							break;
						}
					}
				}
			}
			for(int i=0;i<n;i++){              //move to upside
				int k=0;
				if(numArray[i][j]==0){
					for(k=i+1;k<n;k++){
						if(numArray[k][j]!=0){
							move(i,j,k,j);
							break;
						}
					}
				 }
				 if(k==n)                  //after i all numArray are 0
						break;
				}
			}
	}

	public void moveDown(){
		for(int j=0;j<n;j++){
			for(int i=n-1;i>=0;i--){
				int k=n-1;
				if(numArray[i][j]!=0){
					for(k=i-1;k>=0;k--){
						if(numArray[k][j]!=0&&numArray[i][j]==numArray[k][j]){
							merge(i,j,k,j);
						}
						else if(numArray[k][j]!=0&&numArray[k][j]!=0&&numArray[i][j]!=numArray[k][j]){
							break;
						}
					}
				}
			}
			for(int i=n-1;i>=0;i--){
				int k=n-1;
				if(numArray[i][j]==0){
					for(k=i-1;k>=0;k--){
						if(numArray[k][j]!=0){
							move(i,j,k,j);
							break;
						}
					}
				 }
				 if(k==-1)
						break;
				}
			}
	}

	public void moveLeft(){
		for(int i=0;i<n;i++){
			 for(int j=0;j<n;j++){
				int k=0;
				if(numArray[i][j]!=0){
					for(k=j+1;k<n;k++){
						if(numArray[i][k]!=0&&numArray[i][j]==numArray[i][k]){
							merge(i,j,i,k);
						}
						else if(numArray[i][k]!=0&&numArray[i][k]!=0&&numArray[i][j]!=numArray[i][k]){
							break;
						}
					}
				}
			}
			for(int j=0;j<n;j++){
				int k=0;
				if(numArray[i][j]==0){
					for(k=j+1;k<n;k++){
						if(numArray[i][k]!=0){
							move(i,j,i,k);
							break;
						}
					}
				 }
				 if(k==n)
						break;
				}
			}
	}
	public void moveRight(){
		for(int i=0;i<n;i++){
			 for(int j=n-1;j>=0;j--){
				int k=n-1;
				if(numArray[i][j]!=0){
					for(k=j-1;k>=0;k--){
						if(numArray[i][k]!=0&&numArray[i][j]==numArray[i][k]){
							merge(i,j,i,k);
						}
						else if(numArray[i][k]!=0&&numArray[i][k]!=0&&numArray[i][j]!=numArray[i][k]){
							break;
						}
					}
				}
			}
			for(int j=n-1;j>=0;j--){
				int k=n-1;
				if(numArray[i][j]==0){
					for(k=j-1;k>=0;k--){
						if(numArray[i][k]!=0){
							move(i,j,i,k);
							break;
						}
					}
				 }
				 if(k==-1)
						break;
				}
			}
	}

	public void showResult(){
		if(isWin){
			int result=JOptionPane.showConfirmDialog(this,"You Win! try again?","Result",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.OK_OPTION)
				newGame();
		}
		if(isGameOver){
			int result=JOptionPane.showConfirmDialog(this,"Game Over! try again?","Result",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.OK_OPTION)
				newGame();
		}
	}
	public boolean getGameOver(){
		return isGameOver;
	}
	public void newGame(){
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++){
				btnArray[i][j].setBackground(Color.pink);
				btnArray[i][j].setIcon(null);
				numArray[i][j]=0;
			}
			num=n*n;
			newRandom();
			newRandom();
			isGameOver=false;
			isMoved=false;
			isWin=false;
	}
}

class KeyAction extends KeyAdapter{
	private GamePanel gamePanel;
	public KeyAction(GamePanel gamePanel){
		this.gamePanel=gamePanel;
	}
	@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyChar()==KeyEvent.VK_ESCAPE){
				this.gamePanel.newGame();
			}

			if(this.gamePanel.getGameOver())
				return;
				switch(e.getKeyCode()){
					case KeyEvent.VK_UP:
						this.gamePanel.moveUp();
						break;
					case KeyEvent.VK_DOWN:
						this.gamePanel.moveDown();
						break;
					case KeyEvent.VK_LEFT:
						this.gamePanel.moveLeft();
						break;
					case KeyEvent.VK_RIGHT:
						this.gamePanel.moveRight();
						break;
					case KeyEvent.VK_W:
						this.gamePanel.moveUp();
						break;
					case KeyEvent.VK_S:
						this.gamePanel.moveDown();
						break;
					case KeyEvent.VK_A:
						this.gamePanel.moveLeft();
						break;
					case KeyEvent.VK_D:
						this.gamePanel.moveRight();
						break;
					}
					this.gamePanel.update();
					this.gamePanel.showResult();
			}

		@Override
		public void keyReleased(KeyEvent e){
		}

		@Override
		public void keyTyped(KeyEvent e){
		}
}