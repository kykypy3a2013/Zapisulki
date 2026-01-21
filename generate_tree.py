import os
import sys

def generate_tree(startpath, ignore_list={'.git', '.idea', 'build', '__pycache__'}):
    for root, dirs, files in os.walk(startpath):
        # Исключаем ненужные папки
        dirs[:] = [d for d in dirs if d not in ignore_list]
        level = root.replace(startpath, '').count(os.sep)
        indent = '│   ' * (level - 1) + '├── ' if level > 0 else ''
        print(f'{indent}{os.path.basename(root)}/')
        subindent = '│   ' * level + '├── '
        for i, file in enumerate(files):
            if i == len(files) - 1:
                subindent = '│   ' * level + '└── '
            print(f'{subindent}{file}')

if __name__ == '__main__':
    generate_tree('.')