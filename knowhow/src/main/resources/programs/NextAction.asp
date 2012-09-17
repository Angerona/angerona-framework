% rules for state: 'intentionAdded'
new_act(A)     :- state(intentionAdded), istack([A|R]), is_atomic(A).
new_subgoal(I) :- state(intentionAdded), istack([A|R]), is_atomic(A), 
                  khsubgoal(KH, I, A).
new_khstate([KH|K]) :- state(intentionAdded), khstate(K), 
                       khstatement(KH, I), istack([I|R]), 
                       not khconditionfail(K), not khfailed([KH|K]), 
                       not const_new_act, not multiple.
new_khstate([KH|K]) :- state(intentionAdded), khstate(K), 
                       khstatement(KH, I), khstatement(KHA, I), 
                       istack([I|R]), not khconditionfail(K), 
                       not khfailed([KH|K]), not const_new_act, 
                       multiple, not new_khstate([KHA|K]), 
                       KHA!=KH.
new_khfailed(K) 	:- state(intentionAdded), khstate(K), 
                       not const_new_act, not const_new_khstate.
toparent 			:- const_new_khfailed.

% rules for state: 'actionPerformed'
new_istack([IC|IB]) :- state(actionPerformed), istack([IA|IB]), 
                       khstate([KH|R]), khsubgoal(KH, I, IA), 
                       khsubgoal(KH, J, IC), J=I+1.
new_khperformed(KH) :- state(actionPerformed), khstate([KH|R]), 
                       not khperformed_helper(KH). 
toparent 			:- new_khperformed(X).

% rule for state: 'khAdded'
new_istack([II|IB]) :- state(khAdded), istack(IB), 
                       khsubgoal(KH,1,II), khstate([KH|R]).

% state transition rules:
new_state(actionPerformed) :- new_act(A).
new_state(intentionAdded)  :- new_istack(L), 
                              not const_new_khperformed.
new_state(intentionAdded)  :- const_new_khfailed, 
                              not new_istack([]).
new_state(khAdded)         :- const_new_khstate, 
                              not const_new_khperformed, 
                              not lastknowhowstatement.
new_state(noop)            :- new_istack([]).
new_khstate(K)             :- toparent, khstate([KH|K]).
new_state(noop)            :- state(noop).

% the following rule is responsible to inform the
% system if a condition fails.
khconditionfail(KH) :- khcondition(KH,X), not holds(X).

% a rule which signals that no knowhow statement is left
% and therefore a switch to the parent is senseful.
lastknowhowstatement :- khstate([]), state(actionPerformed).
toparent :- lastknowhowstatement.

% helper rules: Without these rules unsafe rules 
% are created and dlv-complex complains.
const_new_khperformed 	:- new_khperformed(P).
const_new_act 			:- new_act(A).
const_new_khstate 		:- new_khstate(KH).
const_new_khfailed 		:- new_khfailed(KH).
const_new_istack 		:- new_istack(I).
khperformed_helper(KH) 	:- istack([A|R]), khsubgoal(KH,I,A), 
                           khsubgoal(KH,J,_), J=I+1.

% multiple determines which new_khstate rule to use.
multiple :- khstatement(KHA, I), khstatement(KHB, I), 
            istack([I|R]), KHA!=KHB.