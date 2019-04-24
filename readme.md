# Petunjuk Penggunaan Program
Untuk menggunakan program, jalankan Makefile:
```
make run
```
Lalu, masukkan host, port, jumlah peer, dan alamat untuk peer lain yang terhubung

# Pembagian Tugas
13516041 (33%): VersionVector, DeletionBuffer, Debugging, Laporan
13516113 (33%): TextEditor, Controller, Debugging, Laporan
13516116 (33%): CRDT, Networking, Debugging, Laporan

# Cara Kerja Program
Pertama-tama program akan menjalankan sebuah commandline untuk setup peer. Kemudian, akan dibuat sebuah controller untuk masing-masing peer.

Controller ini akan mendengarkan input operasi lokal dan remote. Jika terjadi operasi lokal, controller akan meminta CRDT untuk memasukan/menghapus karakter, lalu mem-broadcast identifier untuk karakter tersebut ke peer lain melalui Messenger. Jika terjadi operasi remote, controller akan berkonsultasi dengan VersionVector untuk operasi tersebut, dan meneruskannya ke ke CRDT jika diperlukan.

CRDT akan menghitung identifier untuk karakter baru tersebut tergantung remote atau lokal sekaligus menempatkan karakter di lokasi yang tepat di TextEditor.

# CRDT
CRDT pada program ini bertugas sebagai struktur data yang menampung list of character yang telah diberikan indentifier. CRDT menangani insert dan delete cara lokal maupun remote. CRDT memastikan bahwa insert dan delete yang terjadi bersifat komutatif dan idempoten. Jika terjadi konflik karena operasi lokal dan remote yang bersamaan, CRDT akan menyelesaikannya dengan hasil solusi yang sama untuk semua peer. Selain itu CRDT memberikan index yang tepat dari sebuah karakter jika index disusun secara linear dan memasukannya ke TextEditor. 

# Version Vector
VersionVector mencatat versi terbaru yang telah diterima dari masing-masing peer. Hal ini diperlukan untuk penanganan deletion yang terjadi sebelum insert untuk karakter yang sama. Hal ini dapat terjadi karena masalah latensi. VersionVector bekerjasama dengan DeletionBuffer untuk menangani masalah ini.

# Deletion Buffer
DeletionBuffer digunakan sebagai tempat penyimpanan perintah delete yang belum memiliki pasangan insert-nya. Jika insert yang memiliki pasangan dengan deletenya, maka insert tersebut tidak perlu dilakukan.

# Analisis

## Solusi yang lebih baik

# Test Case
## Test Case #1: Test local insert
Input:
1. Peer 1 mengetikkan 'd' dan 'e' berurutan
2. Peer 1 mengetikkan 'a' dan 'b' berurutan sebelum huruf 'd'

Output:
```
abcd
```

## Test Case #2: Test local delete
Input:
1. Peer 1 mengetikkan 'abcd'
2. Peer 1 menghapus 'd'
3. Peer 1 menghapus 'b'
4. Peer 1 menghapus 'a'
5. Peer 1 menghapus 'c'

Output:
```

```

## Test Case #3: Test Remote Insert
Input:
1. Peer 1 mengetikkan 'c'
2. Peer 2 mengetikkan 'a' dan 'b' berurutan sebelum huruf 'c'
3. Peer 2 mengetikkan 'e' setelah huruf 'c'
4. Peer 2 mengetikkan 'd' setelah huruf 'c'

Output:
```
abcde
```

## Test Case #4: Test Remote Delete
Input:
1. Peer 1 mengetikkan 'c'
2. Peer 2 mengetikkan 'a' dan 'b' berurutan sebelum huruf 'c'
3. Peer 2 mengetikkan 'e' setelah huruf 'c'
4. Peer 2 mengetikkan 'd' setelah huruf 'c'
5. Peer 1 menghapus 'b', 'd', 'e', 'a', dan 'c' berurutan

Output:
```

```