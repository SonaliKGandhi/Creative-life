#include<stdio.h>
int top=-1;
char stack[50];
void push(char s)
{
	if(top==49)
	{
		printf("overflow\n");
	}
	else
	{
		top=top+1;
		stack[top]=s;	
	}
}
char pop()
{
	if(top==-1)
	{
		printf("underflow\n");
	}
	else
	{
		return(stack[top--]);
	}
}
int prece(char s)
{
	switch(s)
	{
		case '.':return(0);break;
		case '(':
		case ')':return(1);break;
		case '+':
		case '-':return(2);break;
		case '*':
		case '/':
		case '%':return(3);break;
		case '^':
		case '$':return(4);
			 break;
	}	
}
void op(char infix[],char postfix[])
{
	int i,k=0;
	char s;
	stack[++top]='.';
	for(i=0;infix[i]!='\0';i++)
	{
		s=infix[i];
		if(s=='(')
		{
			push(s);
		}
		else if(s=='+'||s=='-'||s=='*'||s=='/'||s=='%'||s=='$'||s=='^')	
		{
			while(prece(stack[top])>=prece(s))
			{
				postfix[k]=pop();
				k++;
			}
			push(s);
		}	
		else if(s==')')
		{
			while(stack[top]!='(')
			{
				postfix[k]=pop();
				k++;
			}
			pop();
		}
		else
		{
			postfix[k]=s;
			k++;
		}
	}
	while(stack[top]!='.')	
		{
			postfix[k]=pop();
			k++;
		}
	postfix[k]='\0';
printf("postfix:%s",postfix);
}


void main()
{
	int i;
	char infix[50],postfix[50];
	printf("enter infix string\n");
	scanf("%s",infix);
	op(infix,postfix);
}
