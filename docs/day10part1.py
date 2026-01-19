from collections import deque
import re

def parse_machine(line):
    dia = re.findall(r"\[(.*?)\]", line)[0]
    target=0
    for i, ch in enumerate(dia):
        if ch =='#':
            target += (1<<i)
    buttons=[]
    for group in re.findall(r"\((.*?\)",line):
        mask=0
        for idx in group.split(','):
            mask += (1<<int(idx))
        buttons.append(mask)
    return target, buttons

def min_presses(target,buttons):
    start=0
    visited= {start}
    queue= deque([start,0])

    while queue:
        state, presses = queue.popleft()
        for b in buttons:
            nxt = state ^ b
            if nxt == target:
                return presses + 1
            if nxt not in visited:
                visited.add(nxt)
                queue.append((nxt,presses+1))

lines = open('input.txt', 'r').read().split('\n')
total=0
for line in lines:
    line = line.strip()
    if line == '':
        continue
    target, buttons = parse_machine(line)
    total += min_presses(target, buttons)
print(total)