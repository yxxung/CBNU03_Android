/*
 *  doubly-linked-list.c
 *
 *  Doubly Linked List
 *
 *  Data Structures
 *  Department of Computer Science 
 *  at Chungbuk National University
 *
 */



#include<stdio.h>
#include<stdlib.h>
/* 필요한 헤더파일 추가 if necessary */


typedef struct Node {			// 이중연결리스트 구조체 생성
	int key;					// 리스트의 데이터 필드 key 생성
	struct Node* llink;			// 이중연결리스트의 왼쪽노드와 연결하는 포인터
	struct Node* rlink;			// 이중연결리스트의 오른쪽 노드와 연결하는 포인터
} listNode;



typedef struct Head {			// 리스트 시작을 나타내는 first 노드를 구조체로 정의
	struct Node* first;
}headNode;

/* 함수 리스트 */

/* note: initialize는 이중포인터를 매개변수로 받음
         singly-linked-list의 initialize와 차이점을 이해 할것 */
int initialize(headNode** h);	// 리스트 생성 후 초기화하는 함수

/* note: freeList는 싱글포인터를 매개변수로 받음
        - initialize와 왜 다른지 이해 할것
        - 이중포인터를 매개변수로 받아도 해제할 수 있을 것 */
int freeList(headNode* h);		// 리스트 할당을 해제하는 함수

int insertNode(headNode* h, int key);	// 조건하는 위치에 노드 삽입하는 함수
int insertLast(headNode* h, int key);	// 마지막 위치에 노드를 삽입하는 함수
int insertFirst(headNode* h, int key);	// 첫번째 위치에 노드를 삽입하는 함수
int deleteNode(headNode* h, int key);	// 원하는 노드의 값을 삭제하는 함수
int deleteLast(headNode* h);			// 마지막 위치의 노드를 삭제하는 함수
int deleteFirst(headNode* h);			// 첫번째 위치의 노드를 삭제하는 함수
int invertList(headNode* h);			// 노드의 위치를 반전시키는 함수

void printList(headNode* h);			// 노드를 보여주는 함수


int main()
{
	char command;
	int key;
	headNode* headnode=NULL;
		printf("\t[-----[이 명 국]  [2017038100]-----]\n");
	do{
		printf("----------------------------------------------------------------\n");
		printf("                     Doubly Linked  List                        \n");
		printf("----------------------------------------------------------------\n");
		printf(" Initialize    = z           Print         = p \n");
		printf(" Insert Node   = i           Delete Node   = d \n");
		printf(" Insert Last   = n           Delete Last   = e\n");
		printf(" Insert First  = f           Delete First  = t\n");
		printf(" Invert List   = r           Quit          = q\n");
		printf("----------------------------------------------------------------\n");

		printf("Command = ");
		scanf(" %c", &command);

		switch(command) {
		case 'z': case 'Z':
			initialize(&headnode);
			break;
		case 'p': case 'P':
			printList(headnode);
			break;
		case 'i': case 'I':
			printf("Your Key = ");
			scanf("%d", &key);
			insertNode(headnode, key);
			break;
		case 'd': case 'D':
			printf("Your Key = ");
			scanf("%d", &key);
			deleteNode(headnode, key);
			break;
		case 'n': case 'N':
			printf("Your Key = ");
			scanf("%d", &key);
			insertLast(headnode, key);
			break;
		case 'e': case 'E':
			deleteLast(headnode);
			break;
		case 'f': case 'F':
			printf("Your Key = ");
			scanf("%d", &key);
			insertFirst(headnode, key);
			break;
		case 't': case 'T':
			deleteFirst(headnode);
			break;
		case 'r': case 'R':
			invertList(headnode);
			break;
		case 'q': case 'Q':
			freeList(headnode);
			break;
		default:
			printf("\n       >>>>>   Concentration!!   <<<<<     \n");
			break;
		}

	}while(command != 'q' && command != 'Q');

	return 1;
}


int initialize(headNode** h) {			// 리스트를 초기화하는 함수 (두방향의 포인터가 있기때문에 이중 포인터로 값을 받는다.)
	if(*h != NULL)
      freeList(*h);

   /* headNode에 대한 메모리를 할당하여 리턴 */
   *h = (headNode*)malloc(sizeof(headNode));		// 헤드 노드 할당
   (*h)->first = NULL;             					// 공백 리스트 이므로 NULL로 지정
	return 1;
}

int freeList(headNode* h){
	listNode* p = h->first;          // 노드 p에 헤드 값 first 지정

   listNode* prev = NULL;           // prev라는 노드에 NULL 지정
   while(p != NULL) {               // 노드 P가 NULL이 아닐때까지 반복
      prev = p;                     
      p = p->rlink;
      free(prev);                   // prev 노드에 할당된 메모리 반환
   }
   free(h);                 
	return 0;
}


void printList(headNode* h) {		// list를 보여주는 함수
	int i = 0;
	listNode* p;					// listnode p 지정

	printf("\n---PRINT\n");

	if(h == NULL) {					// 헤드 노드가 없을시 아래의 printf문 출력
		printf("Nothing to print....\n");
		return;
	}

	p = h->first;					// 시작노드를 p로 지정

	while(p != NULL) {				// p의 값이 있을시 리스트 번호와 리스트 데이터 값 출략
		printf("[ [%d]=%d ] ", i, p->key);
		p = p->rlink;
		i++;
	}

	printf("  items = %d\n", i);
}




/**
 * list에 key에 대한 노드하나를 추가
 */
int insertLast(headNode* h, int key) {	// 마지막 위치에 노드를 추가하는 함수
   listNode* node = (listNode*)malloc(sizeof(listNode));   // 신규 노드 지정 후 node의 데이터 공간을 메모리에 할당 받아 node에 저장.
   listNode* lastNode;         // 노드 존재시 마지막 노드 정의.
   node -> key = key;         // node 필드에 key를 저장
   node ->rlink = NULL;         // key 저장 후 아직 가리키는 노드가 없기때문에 NULL 할당

   if(h -> first == NULL ){   // NULL일 경우
      h -> first = node;      // 시작노드가 node를 가리킴
      return 0;            // 종료
   }
 // 마지막 노드를 찾는 반복문
   lastNode = h -> first;      // 헤드 노드인 h 부터 시작 first를 따라 다음 노드로 이동   
   while(lastNode -> rlink != NULL ){      // lastnode가 NULL이 아닐때까지 반복
      lastNode = lastNode -> rlink;      // 마지막 링크 값을 찾는다.
   }
   lastNode -> rlink = node;         // 해당 사항 없을시 다음 노드로 이동
}

/**
 * list의 마지막 노드 삭제
 */
int deleteLast(headNode* h) {			// 마지막 위치에 노드를 삭제하는 함수
   listNode* frontnode;                // 선행노드 선언
   listNode* delLastnode;              // 삭제하고하는 곳을 가리키는 노드 선언
   if(h -> first == NULL)
   {   // 삭제할 노드가 없을 경우 종료
      printf("삭제할 마지막 노드가 없습니다.\n");
      return 0;
   } 

   else if(h -> first -> rlink == NULL){         // 리스트에 노드가 1개일때
      freeList(h);                  // 할당 메모리 해제
      h -> first = NULL;               // head의 first를 NULL로 지정
      return 0;                     // 종료
   }
   else{                           // 2개 이상의 노드가 있을 경우
      frontnode = h -> first;               // 삭제할 delLastnode 앞의 frontnode를 첫번째 노드로 설정 
      delLastnode = h -> first -> rlink;      // 삭제할 노드를 두번째 노드로 설정
      while(delLastnode -> rlink != NULL){      // 삭제할 노드가 NULL이 아닐때까지 
         frontnode = delLastnode;         // frontnode와 delLastnode를 뒤로 이동
         delLastnode = delLastnode -> rlink;
      }
      free(delLastnode);                  // 마지막 노드 발견시 마지막 노드 메모리 반환
      frontnode -> rlink = NULL;            // frontnode가 delLastnode에 가리키는 링크 NULL 지정
   }	

	return 0;
}



/**
 * list 처음에 key에 대한 노드하나를 추가
 */
int insertFirst(headNode* h, int key) {			// 처음위치에 노드를 추가하는 함수
   listNode* node = (listNode*)malloc(sizeof(listNode));      // 동적 메모리 할당하여 node 생성
   node->key = key;      // node의 데이터 필드에 입력된 데이터 저장

   node->rlink = h->first;   // node의 linke가 h의 first 지정
   h->first = node;         // 시작노드로 node를 지정

	return 0;
}

/**
 * list의 첫번째 노드 삭제
 */
int deleteFirst(headNode* h) {			//처음 위치의 노드를 삭제하는 함수
   listNode* trail;
   if(h -> first == NULL)
   {   // 삭제할 노드가 없을 경우 종료
      printf("삭제할 마지막 노드가 없습니다.\n");
      return 0;
   } 

   else if(h -> first -> rlink == NULL){         // 리스트에 노드가 1개일때
      freeList(h);                  // 할당 메모리 해제
      h -> first = NULL;               // 시작노드를 NULL로 지정
      return 0;                     // 종료
   }
   else{                 
   trail = h -> first;              // trail을 시작노드에 지정
   h -> first = trail ->rlink;       // 시작노드를 trail의 다음 rlink에 지정
   
   }
	return 0;
}



/**
 * 리스트의 링크를 역순으로 재 배치
 */
int invertList(headNode* h) {			// 리스트의 노드를 역순으로 바꾸는 함수
	  listNode* one;                   // 노드 one, two, three 생성
      listNode* two;
      listNode* three;

      one = h -> first;                // one 포인터를 첫번째 노드에 설정
      two = NULL;
      three = NULL;

      while (one != NULL){             // one의 값이 NULL이 아닐동안 반복
         three = two;                  // three를 two가 가리키는 노드에 지정
         two = one;                    // two 노드를 one이 가리키는 노드에 지정
         one = one -> rlink;            // one 노드를 다음 노드로 이동
         two -> rlink = three;          // two 노드의 rlink를 three로 지정
      }
      h -> first = two;                // 시작 노드를 two로 변경
	return 0;
}



/* 리스트를 검색하여, 입력받은 key보다 큰값이 나오는 노드 바로 앞에 삽입 */
int insertNode(headNode* h, int key) {
	
	return 0;
}


/**
 * list에서 key에 대한 노드 삭제
 */
int deleteNode(headNode* h, int key) {
  
  return 1;
}


